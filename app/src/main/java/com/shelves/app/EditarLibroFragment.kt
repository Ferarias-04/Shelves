package com.shelves.app

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.google.android.material.chip.ChipGroup
import com.google.android.material.progressindicator.LinearProgressIndicator

/**
 * Pantalla para editar un libro: página actual, valoración, reseña y lista.
 *
 * Recibe el id del libro por `arguments` (ver [nuevo]), lo busca en [LibrosRepositorio]
 * y, al guardar, modifica ese mismo libro. La barra de progreso se recalcula mientras
 * se escribe la página.
 */
class EditarLibroFragment : Fragment(R.layout.fragment_editar_libro) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val libro = LibrosRepositorio.buscarPorId(requireArguments().getInt(ARG_ID))
        if (libro == null) { // por si acaso: libro que ya no existe
            parentFragmentManager.popBackStack()
            return
        }

        val etPagina = view.findViewById<EditText>(R.id.etPaginaActual)
        val progreso = view.findViewById<LinearProgressIndicator>(R.id.progresoEditar)
        val tvPorcentaje = view.findViewById<TextView>(R.id.tvPorcentajeEditar)
        val rating = view.findViewById<RatingBar>(R.id.ratingLibro)
        val etResena = view.findViewById<EditText>(R.id.etResena)
        val chips = view.findViewById<ChipGroup>(R.id.chipsListaEditar)

        // Rellenamos la pantalla con los datos actuales
        view.findViewById<ImageView>(R.id.imgPortadaEditar)
            .setImageResource(libro.portada ?: R.color.gris_portada)
        view.findViewById<TextView>(R.id.tvTituloEditar).text = libro.titulo
        view.findViewById<TextView>(R.id.tvAutorEditar).text = libro.autor
        view.findViewById<TextView>(R.id.tvPaginasTotales).text =
            getString(R.string.editar_de_paginas, libro.paginasTotales)
        etPagina.setText(libro.paginaActual.toString())
        rating.rating = libro.valoracion
        etResena.setText(libro.resena)
        chips.check(
            when (libro.lista) {
                ListaLibro.LEYENDO -> R.id.chipEditarLeyendo
                ListaLibro.LEIDOS -> R.id.chipEditarLeidos
                ListaLibro.QUIERO_LEER -> R.id.chipEditarQuieroLeer
            }
        )

        // La barra de progreso se actualiza mientras escribes la página
        fun actualizarProgreso() {
            val pagina = (etPagina.text.toString().toIntOrNull() ?: 0).coerceIn(0, libro.paginasTotales)
            val pct = if (libro.paginasTotales > 0) pagina * 100 / libro.paginasTotales else 0
            progreso.progress = pct
            tvPorcentaje.text = getString(R.string.libro_porcentaje, pct)
        }
        actualizarProgreso()
        etPagina.doAfterTextChanged { actualizarProgreso() }

        // Guardar: modificamos el libro en el repositorio y volvemos a la Biblioteca
        view.findViewById<View>(R.id.btnGuardarLibro).setOnClickListener {
            libro.paginaActual = (etPagina.text.toString().toIntOrNull() ?: 0).coerceIn(0, libro.paginasTotales)
            libro.valoracion = rating.rating
            libro.resena = etResena.text.toString().trim()
            libro.lista = when (chips.checkedChipId) {
                R.id.chipEditarLeidos -> ListaLibro.LEIDOS
                R.id.chipEditarQuieroLeer -> ListaLibro.QUIERO_LEER
                else -> ListaLibro.LEYENDO
            }
            Log.d(TAG, "Libro modificado: $libro")
            Toast.makeText(requireContext(), R.string.toast_guardado, Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }

    companion object {
        private const val TAG = "EditarLibro"
        private const val ARG_ID = "id_libro"

        /**
         * Crea la pantalla para un libro concreto. Los fragments reciben sus datos por
         * `arguments` y no por el constructor, porque Android los recrea (por ejemplo, al girar
         * la pantalla) usando el constructor vacío, y los `arguments` sí se conservan.
         *
         * @param idLibro identificador del libro que se va a editar.
         * @return fragment listo para mostrarse.
         */
        fun nuevo(idLibro: Int) = EditarLibroFragment().apply {
            arguments = bundleOf(ARG_ID to idLibro)
        }
    }
}