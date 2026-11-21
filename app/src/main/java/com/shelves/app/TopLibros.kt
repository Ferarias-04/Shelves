package com.shelves.app

/**
 * Géneros de los chips de Inicio.
 *
 * @property nombre recurso de texto del chip.
 */
enum class Genero(val nombre: Int) {
    GENERAL(R.string.fg_home_genero_general),
    TERROR(R.string.fg_home_genero_terror),
    ROMANCE(R.string.fg_home_genero_romance),
    FANTASIA(R.string.fg_home_genero_fantasia),
    DISTOPIA(R.string.fg_home_genero_distopia)
}

/**
 * Libro de la lista "más leídos este mes" de la pantalla de Inicio.
 *
 * @property titulo título del libro.
 * @property autor autor o autora.
 * @property genero género, usado para filtrar con los chips.
 * @property portada recurso de imagen de la portada, o null para mostrar un recuadro gris.
 */
data class LibroTop(
    val titulo: String,
    val autor: String,
    val genero: Genero,
    val portada: Int? = null
)

/** Datos de prueba de los libros más leídos del mes. */
object TopLibros {

    private val libros = listOf(
        LibroTop("Circe", "Madeline Miller", Genero.FANTASIA, R.mipmap.circe_portada),
        LibroTop("Alas de sangre", "Rebecca Yarros", Genero.FANTASIA),
        LibroTop("It", "Stephen King", Genero.TERROR),
        LibroTop("Drácula", "Bram Stoker", Genero.TERROR),
        LibroTop("Orgullo y prejuicio", "Jane Austen", Genero.ROMANCE),
        LibroTop("Romper el círculo", "Colleen Hoover", Genero.ROMANCE),
        LibroTop("1984", "George Orwell", Genero.DISTOPIA),
        LibroTop("Los juegos del hambre", "Suzanne Collins", Genero.DISTOPIA)
    )

    /**
     * Devuelve los libros de un género.
     *
     * @param genero género elegido en los chips. [Genero.GENERAL] devuelve todos.
     * @return lista de libros filtrada.
     */
    fun porGenero(genero: Genero): List<LibroTop> =
        if (genero == Genero.GENERAL) libros else libros.filter { it.genero == genero }
}