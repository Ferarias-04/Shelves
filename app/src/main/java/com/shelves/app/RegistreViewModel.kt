package com.shelves.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel de la pantalla de Registro.
 *
 * Contiene la lógica de la pantalla: comprueba que los seis campos del formulario
 * sean correctos y, si lo son, guarda el usuario nuevo en el repositorio.
 * La Activity solo muestra los datos y avisa al ViewModel cuando se pulsa "Registrarse".
 *
 * El ViewModel sobrevive a los cambios de configuración (por ejemplo, girar el móvil),
 * así que los errores no se pierden: la Activity nueva vuelve a observar los LiveData
 * y los muestra otra vez.
 *
 * Cada LiveData tiene una versión privada (MutableLiveData), que solo el ViewModel puede
 * modificar, y una pública de solo lectura (LiveData), que es la que observa la Activity.
 * Los LiveData de error guardan el id de un string de error (R.string.error_...),
 * o null si el campo es correcto.
 *
 * @see RegistreActivity
 * @see UsuariosRepositorio
 */
class RegistreViewModel : ViewModel() {

    /** Repositorio donde se comprueba si el usuario ya existe y se guardan los usuarios nuevos. */
    private val repositorio = UsuariosRepositorio

    /** Versión modificable de {@link errorUsuario}. Solo se cambia desde este ViewModel. */
    private val _errorUsuario = MutableLiveData<Int?>()

    /** Error del campo usuario: id del string de error, o null si es correcto. */
    val errorUsuario: LiveData<Int?> = _errorUsuario

    /** Versión modificable de {@link errorNombre}. Solo se cambia desde este ViewModel. */
    private val _errorNombre = MutableLiveData<Int?>()

    /** Error del campo nombre: id del string de error, o null si es correcto. */
    val errorNombre: LiveData<Int?> = _errorNombre

    /** Versión modificable de {@link errorApellido}. Solo se cambia desde este ViewModel. */
    private val _errorApellido = MutableLiveData<Int?>()

    /** Error del campo apellido: id del string de error, o null si es correcto. */
    val errorApellido: LiveData<Int?> = _errorApellido

    /** Versión modificable de {@link errorEmail}. Solo se cambia desde este ViewModel. */
    private val _errorEmail = MutableLiveData<Int?>()

    /** Error del campo email: id del string de error, o null si es correcto. */
    val errorEmail: LiveData<Int?> = _errorEmail

    /** Versión modificable de {@link errorContrasena}. Solo se cambia desde este ViewModel. */
    private val _errorContrasena = MutableLiveData<Int?>()

    /** Error del campo contraseña: id del string de error, o null si es correcto. */
    val errorContrasena: LiveData<Int?> = _errorContrasena

    /** Versión modificable de {@link errorConfirmar}. Solo se cambia desde este ViewModel. */
    private val _errorConfirmar = MutableLiveData<Int?>()

    /**
     * Error del campo "confirmar contraseña": id del string de error, o null si es correcto.
     * Además de no estar vacío, tiene que ser igual que la contraseña.
     */
    val errorConfirmar: LiveData<Int?> = _errorConfirmar

    /** Versión modificable de {@link registroCorrecto}. Empieza en false. */
    private val _registroCorrecto = MutableLiveData(false)

    /**
     * Indica si el registro se ha completado.
     * Pasa a true cuando los seis campos son correctos y el usuario se ha guardado;
     * la Activity lo observa para abrir la app (MainActivity).
     */
    val registroCorrecto: LiveData<Boolean> = _registroCorrecto

    /**
     * Valida los datos del formulario, actualiza los LiveData y, si todo es correcto,
     * guarda el usuario en el repositorio.
     *
     * Para cada campo comprueba primero que no esté vacío y después que tenga el formato
     * correcto, usando las funciones de extensión de Validaciones.kt. En el usuario también
     * pregunta al repositorio si ese nombre ya está registrado. El campo "confirmar"
     * se compara con la contraseña.
     *
     * @param usuario texto escrito en el campo usuario.
     * @param nombre texto escrito en el campo nombre.
     * @param apellido texto escrito en el campo apellido.
     * @param email texto escrito en el campo email.
     * @param contrasena texto escrito en el campo contraseña.
     * @param confirmar texto escrito en el campo "confirmar contraseña".
     */
    fun registrar(
        usuario: String,
        nombre: String,
        apellido: String,
        email: String,
        contrasena: String,
        confirmar: String
    ) {
        val errorU = when {
            usuario.isBlank() -> R.string.error_campo_obligatorio
            !usuario.esUsuarioValido() -> R.string.error_usuario_formato
            repositorio.existeUsuario(usuario.trim()) -> R.string.error_usuario_existe
            else -> null
        }
        val errorN = when {
            nombre.isBlank() -> R.string.error_campo_obligatorio
            !nombre.esNombreValido() -> R.string.error_nombre_formato
            else -> null
        }
        val errorA = when {
            apellido.isBlank() -> R.string.error_campo_obligatorio
            !apellido.esNombreValido() -> R.string.error_nombre_formato
            else -> null
        }
        val errorE = when {
            email.isBlank() -> R.string.error_campo_obligatorio
            !email.esEmailValido() -> R.string.error_email_formato
            else -> null
        }
        val errorC = when {
            contrasena.isBlank() -> R.string.error_campo_obligatorio
            !contrasena.esPasswordValida() -> R.string.error_password_formato
            else -> null
        }
        val errorConf = when {
            confirmar.isBlank() -> R.string.error_campo_obligatorio
            confirmar != contrasena -> R.string.error_contrasenas
            else -> null
        }

        _errorUsuario.value = errorU
        _errorNombre.value = errorN
        _errorApellido.value = errorA
        _errorEmail.value = errorE
        _errorContrasena.value = errorC
        _errorConfirmar.value = errorConf

        val todoCorrecto = listOf(errorU, errorN, errorA, errorE, errorC, errorConf)
            .all { it == null }
        if (todoCorrecto) {
            repositorio.registrar(
                Usuario(usuario.trim(), nombre.trim(), apellido.trim(), email.trim(), contrasena)
            )
        }
        _registroCorrecto.value = todoCorrecto
    }
}