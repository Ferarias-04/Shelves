package com.shelves.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


/**
 * ViewModel de la pantalla de Login.
 *
 * Contiene la lógica de la pantalla: decide si el usuario y la contraseña son válidos.
 * La Activity solo muestra los datos y avisa al ViewModel cuando se pulsa "Entrar".
 *
 * El ViewModel sobrevive a los cambios de configuración (por ejemplo, girar el móvil),
 * así que los errores no se pierden: la Activity nueva vuelve a observar los LiveData
 * y los muestra otra vez.
 *
 * Cada LiveData tiene una versión privada (MutableLiveData), que solo el ViewModel puede
 * modificar, y una pública de solo lectura (LiveData), que es la que observa la Activity.
 *
 * @see LoginActivity
 */
class LoginViewModel : ViewModel() {

    /** Versión modificable de {@link errorUsuario}. Solo se cambia desde este ViewModel. */
    private val _errorUsuario = MutableLiveData<Int?>()

    /**
     * Error del campo usuario.
     * Guarda el id de un string de error (R.string.error_...), o null si el campo es correcto.
     * La Activity lo observa y lo muestra en el EditText.
     */
    val errorUsuario: LiveData<Int?> = _errorUsuario

    /** Versión modificable de {@link errorContrasena}. Solo se cambia desde este ViewModel. */
    private val _errorContrasena = MutableLiveData<Int?>()

    /**
     * Error del campo contraseña.
     * Funciona igual que {@link errorUsuario}: id de un string de error, o null si es correcto.
     */
    val errorContrasena: LiveData<Int?> = _errorContrasena

    /** Versión modificable de {@link loginCorrecto}. Empieza en false. */
    private val _loginCorrecto = MutableLiveData(false)

    /**
     * Indica si el login es válido.
     * Pasa a true cuando los dos campos son correctos; la Activity lo observa
     * para abrir la app (MainActivity).
     */
    val loginCorrecto: LiveData<Boolean> = _loginCorrecto

    /**
     * Valida los datos que ha escrito el usuario y actualiza los LiveData con el resultado.
     *
     * Para cada campo comprueba primero que no esté vacío y después que tenga el formato
     * correcto, usando las funciones de extensión de Validaciones.kt.
     * Todavía no hay usuarios reales, así que basta con que el formato sea correcto.
     *
     * @param usuario texto escrito en el campo usuario.
     * @param contrasena texto escrito en el campo contraseña.
     */
    fun login(usuario: String, contrasena: String) {
        val errorU = when {
            usuario.isBlank() -> R.string.error_campo_obligatorio
            !usuario.esUsuarioValido() -> R.string.error_usuario_formato
            else -> null
        }
        val errorC = when {
            contrasena.isBlank() -> R.string.error_campo_obligatorio
            !contrasena.esPasswordValida() -> R.string.error_password_formato
            else -> null
        }
        _errorUsuario.value = errorU
        _errorContrasena.value = errorC
        _loginCorrecto.value = errorU == null && errorC == null
    }
}