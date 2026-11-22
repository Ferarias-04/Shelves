package com.shelves.app

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Holder de una portada de la lista "Libros más leídos este mes".
 *
 * Guarda las vistas de item_portada.xml (imagen y título) para no buscarlas con
 * findViewById cada vez que la portada se reutiliza al hacer scroll.
 *
 * @param itemView vista de la portada ya inflada.
 */
class PortadaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val imgPortada: ImageView = itemView.findViewById(R.id.imgPortadaTop)
    private val tvTitulo: TextView = itemView.findViewById(R.id.tvTituloTop)

    /**
     * Pone los datos del libro en la portada y le asigna el evento de click.
     *
     * @param libro libro que se muestra.
     * @param onClick acción al tocar la portada.
     */
    fun bind(libro: LibroTop, onClick: (LibroTop) -> Unit) {
        imgPortada.setImageResource(libro.portada ?: R.color.gris_portada)
        tvTitulo.text = libro.titulo
        itemView.contentDescription =
            itemView.context.getString(R.string.top_desc, libro.titulo, libro.autor)
        itemView.setOnClickListener { onClick(libro) }
    }
}