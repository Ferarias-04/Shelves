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
 * Pantalla de inicio de sesión. Se abre después del Splash.
 *
 * Comprueba que el usuario y la contraseña no estén vacíos antes de entrar en la app
 * ([MainActivity]). Todavía no hay usuarios reales, así que cualquier dato sirve.
 * También da acceso a la pantalla de registro ([RegistreActivity]).
 */
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val etUsuario = findViewById<EditText>(R.id.etUsuario)
        val etContrasena = findViewById<EditText>(R.id.etContrasena)

        //Entrar: si los campos están rellenados, abrimos la app
        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            if(camposRellenos(etUsuario, etContrasena)) {
                startActivity(Intent(this, MainActivity::class.java))
                finish() //al pulsar "atras" desde la app no se vuelve al login
            }
        }
        //ir al registro
        findViewById<Button>(R.id.btnSignUp).setOnClickListener {
            startActivity(Intent(this, RegistreActivity::class.java))
        }
    }
    /**
     * Revisa que los campos tengan texto. Los que están vacíos se marcan con un error en rojo,
     * para que el usuario vea qué le falta sin tener que adivinarlo.
     *
     * @param campos campos de texto que hay que comprobar.
     * @return true si todos están rellenos, false si falta alguno.
     */
    private fun camposRellenos(vararg campos: EditText): Boolean {
        var ok = true
        for(campo in campos) {
            if(campo.text.isNullOrBlank()) {
                campo.error = getString(R.string.error_campo_obligatorio)
                ok = false
            }
        }
        return ok
    }
}