package com.shelves.app

/**
 * Libros de prueba de la app.
 * Es un `object`: solo existe una instancia en toda la app, así que todas las
 * pantallas (Biblioteca, Editar libro…) trabajan con los mismos datos.
 */
object LibrosRepositorio {

    private var siguienteId = 1
    private val libros = mutableListOf<Libro>()

    init {
        // Leyendo
        anadir("Circe", "Madeline Miller", 350, 120, ListaLibro.LEYENDO, R.mipmap.circe_portada)
        anadir("El nombre del viento", "Patrick Rothfuss", 880, 412, ListaLibro.LEYENDO, R.drawable.portada_nombre_viento)
        anadir("Sapiens", "Yuval Noah Harari", 496, 60, ListaLibro.LEYENDO, R.drawable.portada_sapiens)
        // Leídos
        anadir("La canción de Aquiles", "Madeline Miller", 384, 384, ListaLibro.LEIDOS, R.drawable.portada_cancion_aquiles)
        anadir("Orgullo y prejuicio", "Jane Austen", 432, 432, ListaLibro.LEIDOS, R.drawable.portada_orgullo_prejuicio)
        anadir("Cien años de soledad", "Gabriel García Márquez", 496, 496, ListaLibro.LEIDOS, R.drawable.portada_anos_soledad)
        // Quiero leer
        anadir("Babel", "R. F. Kuang", 560, portada= R.drawable.portada_babel)
        anadir("El infinito en un junco", "Irene Vallejo", 452, portada= R.drawable.portada_infinito_junco)
        anadir("Proyecto Hail Mary", "Andy Weir", 496, portada= R.drawable.portada_hil_mary)
    }

    /**
     * Crea un libro con un id nuevo y lo guarda en el repositorio.
     *
     * @param titulo título del libro.
     * @param autor autor o autora.
     * @param paginasTotales número de páginas del libro.
     * @param paginaActual página por la que va leyendo (0 si no ha empezado).
     * @param lista lista a la que pertenece; por defecto, Quiero leer.
     * @param portada recurso de la imagen de portada, o null si no tiene.
     * @return el libro recién creado.
     */
    fun anadir(
        titulo: String,
        autor: String,
        paginasTotales: Int,
        paginaActual: Int = 0,
        lista: ListaLibro = ListaLibro.QUIERO_LEER,
        portada: Int? = null
    ): Libro {
        val libro = Libro(siguienteId++, titulo, autor, paginasTotales, paginaActual, lista, portada = portada)
        libros.add(libro)
        return libro
    }

    fun buscarPorId(id: Int): Libro? = libros.find { it.id == id }

    fun eliminar(libro: Libro) {
        libros.removeAll { it.id == libro.id }
    }

    /**
     * Devuelve los libros de una lista cuyo título o autor contiene el texto buscado.
     *
     * @param lista lista que se está viendo (Leyendo, Leídos o Quiero leer).
     * @param texto texto del buscador; si está vacío, no se filtra por texto.
     * @return libros que cumplen las dos condiciones, sin distinguir mayúsculas.
     */
    fun filtrar(lista: ListaLibro, texto: String): List<Libro> =
        libros.filter {
            it.lista == lista &&
                    (texto.isBlank() ||
                            it.titulo.contains(texto, ignoreCase = true) ||
                            it.autor.contains(texto, ignoreCase = true))
        }
}