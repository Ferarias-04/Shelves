package com.shelves.app

/** Las tres listas de la biblioteca. Cada una guarda el texto que se muestra en pantalla. */
enum class ListaLibro(val nombre: Int) {
    LEYENDO(R.string.biblioteca_leyendo),
    LEIDOS(R.string.biblioteca_leidos),
    QUIERO_LEER(R.string.biblioteca_quiero_leer)
}

/** Un libro de la biblioteca del usuario. */
data class Libro(
    val id: Int,
    val titulo: String,
    val autor: String,
    val paginasTotales: Int,
    var paginaActual: Int = 0,
    var lista: ListaLibro = ListaLibro.QUIERO_LEER,
    var valoracion: Float = 0f,
    var resena: String = "",
    val portada: Int? = null // recurso de imagen; si es null se ve el recuadro gris
) {
    /** Porcentaje leído (0-100), calculado a partir de las páginas. */
    val porcentaje: Int
        get() = if (paginasTotales > 0) paginaActual * 100 / paginasTotales else 0
}