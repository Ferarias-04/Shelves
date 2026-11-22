package com.shelves.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Pantalla de registro de un usuario nuevo.
 *
 * Al pulsar "Registrarse" comprueba que todos los campos estén rellenos y que las dos
 * contraseñas coincidan; si algo falla, el error se muestra en el propio campo.
 * Si todo está bien, abre [MainActivity] y borra Login y Registro de la pila de pantallas,
 * para que el botón "atrás" no vuelva al formulario.
 */
class RegistreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registre)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val campos = listOf<EditText>(
            findViewById(R.id.etRegUsuario),
            findViewById(R.id.etRegNombre),
            findViewById(R.id.etRegApellido),
            findViewById(R.id.etRegEmail),
            findViewById(R.id.etRegContrasena),
            findViewById(R.id.etRegConfirmar)
        )
        val etContrasena = findViewById<EditText>(R.id.etRegContrasena)
        val etConfirmar = findViewById<EditText>(R.id.etRegConfirmar)

        findViewById<Button>(R.id.btnRegistro).setOnClickListener {
            var ok = true
            for(campo in campos) {
                if(campo.text.isNullOrBlank()) {
                    campo.error = getString(R.string.error_campo_obligatorio)
                    ok = false
                }
            }
            if(ok && etContrasena.text.toString() != etConfirmar.text.toString()) {
                etConfirmar.error = getString(R.string.error_contrasenas)
                ok = false
            }
            if(ok) {
                //abrimos la app y borramos login y registro de la pila,
                //así "atrás" desde la app la cierra en vez de volver aquí
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
        //volver al login: cerramos esta pantalla y reaparece la de abajo
        findViewById<Button>(R.id.btnVolverLogin).setOnClickListener {
            finish()
        }
    }
}