package com.shelves.app

import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.LinearProgressIndicator

/**
 * Holder: guarda las vistas de UNA fila (item_libro.xml) para no tener que buscarlas
 * con findViewById cada vez que se reutiliza la fila al hacer scroll.
 * @param itemView vista de la fila ya inflada.
 */
class LibroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val card = itemView as MaterialCardView
    private val imgPortada: ImageView = itemView.findViewById(R.id.imgPortada)
    private val tvTitulo: TextView = itemView.findViewById(R.id.tvTituloLibro)
    private val tvAutor: TextView = itemView.findViewById(R.id.tvAutorLibro)
    private val progreso: LinearProgressIndicator = itemView.findViewById(R.id.progresoLibro)
    private val tvPorcentaje: TextView = itemView.findViewById(R.id.tvPorcentaje)
    private val tvPaginas: TextView = itemView.findViewById(R.id.tvPaginas)

    // Borde original de la tarjeta, para restaurarlo al quitar la selección
    private val grosorBorde = card.strokeWidth
    private val colorBorde = card.strokeColorStateList

    /**
     * Rellena la fila con los datos de un libro y le asigna los eventos. Si el libro está
     * seleccionado, dibuja el borde grueso y verde. También da a TalkBack la descripción
     * de la fila y de lo que hace cada gesto.
     *
     * @param libro libro que se muestra en la fila.
     * @param seleccionado true si es el libro marcado con pulsación larga.
     * @param onClick acción al tocar la fila.
     * @param onLongClick acción al mantener pulsada la fila.
     */
    fun bind(
        libro: Libro,
        seleccionado: Boolean,
        onClick: (Libro) -> Unit,
        onLongClick: (Libro) -> Unit
    ) {
        val ctx = itemView.context

        tvTitulo.text = libro.titulo
        tvAutor.text = libro.autor
        progreso.progress = libro.porcentaje
        tvPorcentaje.text = ctx.getString(R.string.libro_porcentaje, libro.porcentaje)
        tvPaginas.text = ctx.getString(R.string.libro_paginas, libro.paginaActual, libro.paginasTotales)
        imgPortada.setImageResource(libro.portada ?: R.color.gris_portada)

        // Libro seleccionado: borde más grueso y de color verde
        if (seleccionado) {
            card.strokeWidth = grosorBorde * 3
            card.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(ctx, R.color.shelves_primary)))
        } else {
            card.strokeWidth = grosorBorde
            card.setStrokeColor(colorBorde)
        }

        // Eventos
        itemView.setOnClickListener { onClick(libro) }
        itemView.setOnLongClickListener {
            onLongClick(libro)
            true // true = evento consumido, no se lanza también el click normal
        }

        // Accesibilidad: TalkBack lee la fila entera de una vez y anuncia qué hace cada gesto
        itemView.isSelected = seleccionado
        itemView.contentDescription =
            ctx.getString(R.string.libro_desc, libro.titulo, libro.autor, libro.porcentaje)
        ViewCompat.replaceAccessibilityAction(
            itemView, AccessibilityActionCompat.ACTION_CLICK,
            ctx.getString(R.string.libro_accion_editar), null
        )
        ViewCompat.replaceAccessibilityAction(
            itemView, AccessibilityActionCompat.ACTION_LONG_CLICK,
            ctx.getString(R.string.libro_accion_seleccionar), null
        )
    }
}