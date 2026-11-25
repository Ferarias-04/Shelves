package com.shelves.app

/**
 * Repositorio de usuarios: es el único sitio de la app que guarda y consulta usuarios.
 *
 * Los ViewModels no saben dónde están guardados los datos; solo llaman a estas funciones.
 * Por ahora los usuarios se guardan en una lista en memoria (se borran al cerrar la app).
 * Más adelante se cambiará por una base de datos o una API, y solo habrá que modificar
 * esta clase, no los ViewModels.
 *
 * Es un object: solo existe una instancia, así que Login y Registro comparten los mismos datos.
 *
 * @see LoginViewModel
 * @see RegistreViewModel
 */
object UsuariosRepositorio {

    /** Lista de usuarios registrados. Solo se modifica desde este repositorio. */
    private val usuarios = mutableListOf<Usuario>()

    /**
     * Crea un usuario de prueba para poder iniciar sesión sin registrarse antes.
     * Usuario: demo. Contraseña: shelves2026.
     */
    init {
        usuarios.add(Usuario("demo", "Demo", "Shelves", "demo@shelves.com", "shelves2026"))
    }

    /**
     * Comprueba si ya hay un usuario registrado con ese nombre de usuario.
     * No distingue mayúsculas de minúsculas.
     *
     * @param nombreUsuario nombre de usuario que se quiere comprobar.
     * @return true si ya existe, false si está libre.
     */
    fun existeUsuario(nombreUsuario: String): Boolean =
        usuarios.any { it.usuario.equals(nombreUsuario, ignoreCase = true) }

    /**
     * Guarda un usuario nuevo.
     *
     * @param usuario datos del usuario que se quiere registrar.
     * @return true si se ha guardado, false si ese nombre de usuario ya existía.
     */
    fun registrar(usuario: Usuario): Boolean {
        if (existeUsuario(usuario.usuario)) return false
        usuarios.add(usuario)
        return true
    }

    /**
     * Comprueba si el usuario y la contraseña corresponden a una cuenta registrada.
     *
     * @param nombreUsuario nombre de usuario escrito en el login.
     * @param contrasena contraseña escrita en el login.
     * @return true si coinciden con un usuario guardado, false si no.
     */
    fun comprobarLogin(nombreUsuario: String, contrasena: String): Boolean =
        usuarios.any { it.usuario.equals(nombreUsuario, ignoreCase = true) && it.contrasena == contrasena }
}