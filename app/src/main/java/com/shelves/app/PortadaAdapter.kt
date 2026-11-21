package com.shelves.app

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter de la lista horizontal "Libros más leídos este mes".
 *
 * @param onClick acción al tocar una portada; recibe el libro tocado.
 */
class PortadaAdapter(
    private val onClick: (LibroTop) -> Unit
) : RecyclerView.Adapter<PortadaViewHolder>() {

    private val libros = mutableListOf<LibroTop>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortadaViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_portada, parent, false)
        return PortadaViewHolder(vista)
    }

    override fun onBindViewHolder(holder: PortadaViewHolder, position: Int) {
        holder.bind(libros[position], onClick)
    }

    override fun getItemCount(): Int = libros.size

    /**
     * Sustituye los libros que se muestran (al cambiar de género).
     *
     * @param nuevos libros a mostrar.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun mostrar(nuevos: List<LibroTop>) {
        libros.clear()
        libros.addAll(nuevos)
        notifyDataSetChanged()
    }
}