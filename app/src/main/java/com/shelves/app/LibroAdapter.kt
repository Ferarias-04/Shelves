package com.shelves.app

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter: conecta la lista de libros con el RecyclerView.
 * - onCreateViewHolder: crea una fila nueva (solo las que caben en pantalla + unas pocas).
 * - onBindViewHolder: pone los datos de un libro en una fila ya creada (se reutilizan).
 * - getItemCount: cuántos libros hay.
 * Los eventos se reciben como funciones (lambdas) para que la lógica quede en el Fragment.
 */
class LibroAdapter(
    private val onClick: (Libro) -> Unit,
    private val onLongClick: (Libro) -> Unit
) : RecyclerView.Adapter<LibroViewHolder>() {

    private val libros = mutableListOf<Libro>()

    /** Libro marcado con mantener pulsado (null = ninguno). */
    var seleccionado: Libro? = null
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibroViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_libro, parent, false)
        return LibroViewHolder(vista)
    }

    override fun onBindViewHolder(holder: LibroViewHolder, position: Int) {
        val libro = libros[position]
        holder.bind(libro, libro.id == seleccionado?.id, onClick, onLongClick)
    }

    override fun getItemCount(): Int = libros.size

    /** Sustituye todos los libros (al cambiar de chip o buscar). */
    @SuppressLint("NotifyDataSetChanged") // cambia la lista entera, así que redibujamos todo
    fun mostrar(nuevos: List<Libro>) {
        libros.clear()
        libros.addAll(nuevos)
        notifyDataSetChanged()
    }

    fun anadir(libro: Libro) {
        libros.add(libro)
        notifyItemInserted(libros.size - 1) // solo se anima la fila nueva
    }

    fun eliminar(libro: Libro) {
        val pos = posicionDe(libro)
        if (pos >= 0) {
            libros.removeAt(pos)
            notifyItemRemoved(pos) // la fila desaparece con animación
        }
    }

    fun seleccionar(libro: Libro?) {
        val anterior = seleccionado
        seleccionado = libro
        // Redibujamos solo las dos filas afectadas
        anterior?.let { refrescar(it) }
        libro?.let { refrescar(it) }
    }

    private fun refrescar(libro: Libro) {
        val pos = posicionDe(libro)
        if (pos >= 0) notifyItemChanged(pos)
    }

    private fun posicionDe(libro: Libro) = libros.indexOfFirst { it.id == libro.id }
}