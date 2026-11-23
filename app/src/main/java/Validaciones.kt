package com.shelves.app

import android.util.Patterns

/*
 * Funciones de extensión para validar los campos de Login y Registro.
 * Se llaman directamente sobre el texto, por ejemplo: email.esEmailValido().
 * Las usan los dos ViewModel, así ambas pantallas validan con las mismas reglas.
 */

/** Usuario: de 3 a 20 caracteres; solo letras, números, punto o guion bajo. */
fun String.esUsuarioValido(): Boolean =
    Regex("^[A-Za-z0-9._]{3,20}$").matches(trim())

/** Email con formato correcto (algo@dominio.ext). */
fun String.esEmailValido(): Boolean =
    Patterns.EMAIL_ADDRESS.matcher(trim()).matches()

/** Contraseña: mínimo 8 caracteres, con al menos una letra y un número. */
fun String.esPasswordValida(): Boolean =
    length >= 8 && any { it.isLetter() } && any { it.isDigit() }

/** Nombre o apellido: letras (con tildes), espacios, apóstrofo o guion; de 2 a 40 caracteres. */
fun String.esNombreValido(): Boolean =
    Regex("^[\\p{L} '-]{2,40}$").matches(trim())