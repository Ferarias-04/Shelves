package com.shelves.app

/** Las tres listas de la biblioteca. Cada una guarda el texto que se muestra en pantalla.
 *
 * @property nombre recurso de texto con el nombre de la lista tal como se ve en pantalla.
 * */
enum class ListaLibro(val nombre: Int) {
    LEYENDO(R.string.biblioteca_leyendo),
    LEIDOS(R.string.biblioteca_leidos),
    QUIERO_LEER(R.string.biblioteca_quiero_leer)
}

/**
 * Un libro de la biblioteca del usuario.
 *
 * @property id identificador único del libro.
 * @property titulo título del libro.
 * @property autor autor o autora.
 * @property paginasTotales número total de páginas.
 * @property paginaActual página por la que va leyendo (se puede modificar).
 * @property lista lista a la que pertenece: Leyendo, Leídos o Quiero leer (se puede modificar).
 * @property valoracion puntuación en estrellas que le da el usuario.
 * @property resena reseña escrita por el usuario.
 * @property portada recurso de la imagen de portada; si es null se ve un recuadro gris.
 */
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