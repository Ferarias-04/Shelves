package com.shelves.app

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import androidx.core.view.isVisible
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Pestaña Biblioteca: muestra los libros del usuario en un RecyclerView.
 *
 * Los chips cambian la lista (Leyendo, Leídos, Quiero leer) y el buscador filtra por título
 * o autor. Al tocar un libro se abre [EditarLibroFragment]. Al mantenerlo pulsado se selecciona
 * y aparece la barra de edición, desde la que se puede añadir, mover o eliminar libros.
 */
class BibliotecaFragment : Fragment(R.layout.fragment_biblioteca) {

    private lateinit var adapter: LibroAdapter
    private lateinit var etBuscar: EditText
    private var listaActual = ListaLibro.LEYENDO
    private lateinit var rvLibros: RecyclerView
    private lateinit var barraEdicion: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = LibroAdapter(
            onClick = { libro ->
                if (adapter.seleccionado != null) seleccionar(libro) else abrirEditar(libro)
            },
            onLongClick = { libro -> seleccionar(libro) }
        )
        rvLibros = view.findViewById(R.id.rvLibros)
        rvLibros.adapter = adapter // el LayoutManager está en el XML

        // Chips: cambian la lista que se muestra
        view.findViewById<ChipGroup>(R.id.chipsListas).setOnCheckedStateChangeListener { _, ids ->
            listaActual = when (ids.firstOrNull()) {
                R.id.chipLeidos -> ListaLibro.LEIDOS
                R.id.chipQuieroLeer -> ListaLibro.QUIERO_LEER
                else -> ListaLibro.LEYENDO
            }
            salirModoSeleccion()
            cargarLibros()
        }
        etBuscar = view.findViewById(R.id.etBuscar)
        etBuscar.doAfterTextChanged {
            salirModoSeleccion()
            cargarLibros()
        }

        barraEdicion = view.findViewById(R.id.barraEdicion)
        view.findViewById<View>(R.id.btnAnadir).setOnClickListener { dialogoAnadir() }
        view.findViewById<View>(R.id.btnMover).setOnClickListener {
            adapter.seleccionado?.let { dialogoMover(it) }
        }
        view.findViewById<View>(R.id.btnEliminar).setOnClickListener {
            adapter.seleccionado?.let { dialogoEliminar(it) }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, atrasConSeleccion)

        cargarLibros()
    }

    private fun cargarLibros() {
        adapter.mostrar(LibrosRepositorio.filtrar(listaActual, etBuscar.text.toString()))
    }

    private fun abrirEditar(libro: Libro) {
        Log.d(TAG, "Editar: ${libro.titulo}")
        (requireActivity() as MainActivity).abrirSeccion(EditarLibroFragment.nuevo(libro.id))
    }

    private val atrasConSeleccion = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            val main = requireActivity() as MainActivity
            if (main.menuAbierto()) main.cerrarMenu() else salirModoSeleccion()
        }
    }
    override fun onDestroyView() {
        // Si se sale de la pantalla en modo selección, se devuelve la barra de navegación
        (requireActivity() as MainActivity).mostrarBarraNavegacion(true)
        atrasConSeleccion.isEnabled = false
        super.onDestroyView()
    }

    /**
     * Selecciona un libro y muestra la barra de edición. Si ya estaba seleccionado,
     * sale del modo selección.
     *
     * @param libro libro que se ha tocado o mantenido pulsado.
     */
    private fun seleccionar(libro: Libro) {
        if (adapter.seleccionado?.id == libro.id) {
            salirModoSeleccion()
            return
        }
        Log.d(TAG, "Seleccionado: ${libro.titulo}")
        adapter.seleccionar(libro)
        mostrarBarraEdicion(true)
    }

    private fun salirModoSeleccion() {
        adapter.seleccionar(null)
        mostrarBarraEdicion(false)
    }

    private fun mostrarBarraEdicion(visible: Boolean) {
        barraEdicion.isVisible = visible
        (requireActivity() as MainActivity).mostrarBarraNavegacion(!visible)
        atrasConSeleccion.isEnabled = visible
    }

    /**
     * Evento AÑADIR: abre un diálogo para crear un libro nuevo (el título es obligatorio).
     * Se guarda en [LibrosRepositorio] dentro de la lista que se está viendo y se inserta
     * en el RecyclerView con animación.
     */
    private fun dialogoAnadir() {
        val vista = layoutInflater.inflate(R.layout.anadir_libro, null)
        val etTitulo = vista.findViewById<EditText>(R.id.etNuevoTitulo)
        val etAutor = vista.findViewById<EditText>(R.id.etNuevoAutor)
        val etPaginas = vista.findViewById<EditText>(R.id.etNuevoPaginas)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.edicion_anadir)
            .setView(vista)
            .setNegativeButton(R.string.cancelar, null)
            .setPositiveButton(R.string.anadir) { _, _ ->
                val titulo = etTitulo.text.toString().trim()
                if (titulo.isEmpty()) {
                    Toast.makeText(requireContext(), R.string.error_titulo_obligatorio, Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                val paginas = etPaginas.text.toString().toIntOrNull() ?: 0
                val leidas = if (listaActual == ListaLibro.LEIDOS) paginas else 0

                etBuscar.text?.clear() // para que el libro nuevo se vea seguro
                val libro = LibrosRepositorio.anadir(
                    titulo, etAutor.text.toString().trim(), paginas, leidas, listaActual
                )
                adapter.anadir(libro)
                rvLibros.scrollToPosition(adapter.itemCount - 1)
                salirModoSeleccion()

                Log.d(TAG, "Añadido: $libro")
                avisar(getString(R.string.toast_anadido, libro.titulo, getString(listaActual.nombre)))
            }
            .show()
    }
    /**
     * Evento MODIFICAR: permite cambiar el libro de lista. Como deja de pertenecer a la lista
     * visible, se quita del RecyclerView. Si pasa a Leídos, se marcan todas sus páginas como leídas.
     *
     * @param libro libro seleccionado.
     */
    private fun dialogoMover(libro: Libro) {
        val listas = ListaLibro.entries
        val nombres = listas.map { getString(it.nombre) }.toTypedArray()

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.edicion_mover)
            .setSingleChoiceItems(nombres, listas.indexOf(libro.lista)) { dialogo, posicion ->
                val nueva = listas[posicion]
                if (nueva != libro.lista) {
                    libro.lista = nueva
                    if (nueva == ListaLibro.LEIDOS) libro.paginaActual = libro.paginasTotales
                    adapter.eliminar(libro)

                    Log.d(TAG, "Movido: $libro")
                    avisar(getString(R.string.toast_movido, libro.titulo, getString(nueva.nombre)))
                }
                dialogo.dismiss()
                salirModoSeleccion()
            }
            .setNegativeButton(R.string.cancelar, null)
            .show()
    }
    /**
     * Evento ELIMINAR: pide confirmación y, si se acepta, borra el libro del repositorio
     * y de la lista.
     *
     * @param libro libro seleccionado.
     */
    private fun dialogoEliminar(libro: Libro) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.eliminar_titulo)
            .setMessage(getString(R.string.eliminar_mensaje, libro.titulo))
            .setNegativeButton(R.string.cancelar, null)
            .setPositiveButton(R.string.eliminar) { _, _ ->
                LibrosRepositorio.eliminar(libro)
                adapter.eliminar(libro)
                salirModoSeleccion()

                Log.d(TAG, "Eliminado: $libro")
                avisar(getString(R.string.toast_eliminado, libro.titulo))
            }
            .show()
    }

    private fun avisar(mensaje: String) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "Biblioteca"
    }
}