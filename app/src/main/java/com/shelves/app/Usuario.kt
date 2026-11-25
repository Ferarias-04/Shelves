package com.shelves.app

/**
 * Datos de un usuario de la app. Es la "M" (Model) del patrón MVVM.
 *
 * @property usuario nombre de usuario con el que inicia sesión.
 * @property nombre nombre de la persona.
 * @property apellido apellido de la persona.
 * @property email correo electrónico.
 * @property contrasena contraseña de la cuenta.
 */
data class Usuario(
    val usuario: String,
    val nombre: String,
    val apellido: String,
    val email: String,
    val contrasena: String
)