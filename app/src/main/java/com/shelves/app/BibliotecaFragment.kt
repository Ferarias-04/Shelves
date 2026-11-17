package com.shelves.app

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup

/** Pestaña Biblioteca: lista de libros filtrada por lista (chips) y por texto (buscador). */
class BibliotecaFragment : Fragment(R.layout.fragment_biblioteca) {

    private lateinit var adapter: LibroAdapter
    private lateinit var etBuscar: EditText
    private var listaActual = ListaLibro.LEYENDO

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = LibroAdapter(
            onClick = { libro -> abrirEditar(libro) },
            onLongClick = { libro -> Log.d(TAG, "Mantener pulsado: ${libro.titulo}") } // en 5B
        )
        // El LayoutManager ya está puesto en el XML (app:layoutManager)
        view.findViewById<RecyclerView>(R.id.rvLibros).adapter = adapter

        // Chips: cambian la lista que se muestra
        view.findViewById<ChipGroup>(R.id.chipsListas).setOnCheckedStateChangeListener { _, ids ->
            listaActual = when (ids.firstOrNull()) {
                R.id.chipLeidos -> ListaLibro.LEIDOS
                R.id.chipQuieroLeer -> ListaLibro.QUIERO_LEER
                else -> ListaLibro.LEYENDO
            }
            cargarLibros()
        }

        // Buscador: filtra mientras escribes
        etBuscar = view.findViewById(R.id.etBuscar)
        etBuscar.doAfterTextChanged { cargarLibros() }

        cargarLibros()
    }

    private fun cargarLibros() {
        adapter.mostrar(LibrosRepositorio.filtrar(listaActual, etBuscar.text.toString()))
    }

    private fun abrirEditar(libro: Libro) {
        Log.d(TAG, "Editar: ${libro.titulo}")
        (requireActivity() as MainActivity).abrirSeccion(EditarLibroFragment.nuevo(libro.id))
    }

    companion object {
        private const val TAG = "Biblioteca"
    }
}