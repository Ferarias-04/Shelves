package com.shelves.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Pantalla de registro de un usuario nuevo.
 *
 * Sigue el patrón MVVM: esta Activity es la Vista. No decide si los datos son correctos;
 * le pasa el texto de los campos a RegistreViewModel y observa sus LiveData para mostrar
 * los errores o para entrar en la app.
 *
 * @see RegistreViewModel
 * @see LoginActivity
 */
class RegistreActivity : AppCompatActivity() {

    /**
     * ViewModel de esta pantalla.
     * by viewModels() lo crea la primera vez y, si la Activity se recrea
     * (por ejemplo, al girar el móvil), devuelve el mismo objeto.
     */
    private val viewModel: RegistreViewModel by viewModels()

    /**
     * Prepara la pantalla.
     *
     * Primero se suscribe a los LiveData del ViewModel con observe(): cada vez que
     * el ViewModel cambia un valor, se ejecuta el bloque correspondiente.
     * <ul>
     *   <li>Los seis LiveData de error: muestran el mensaje en su campo,
     *       o lo quitan si el valor es null.</li>
     *   <li>registroCorrecto: si es true, abre MainActivity y borra Login y Registro
     *       de la pila de pantallas, para que el botón "atrás" no vuelva al formulario.</li>
     * </ul>
     * Después configura los botones: "Registrarse" envía los datos al ViewModel para que
     * los valide, y "Volver" cierra esta pantalla y vuelve al login.
     *
     * @param savedInstanceState estado guardado si la Activity se está recreando, o null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registre)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val etUsuario = findViewById<EditText>(R.id.etRegUsuario)
        val etNombre = findViewById<EditText>(R.id.etRegNombre)
        val etApellido = findViewById<EditText>(R.id.etRegApellido)
        val etEmail = findViewById<EditText>(R.id.etRegEmail)
        val etContrasena = findViewById<EditText>(R.id.etRegContrasena)
        val etConfirmar = findViewById<EditText>(R.id.etRegConfirmar)

        viewModel.errorUsuario.observe(this) { error ->
            etUsuario.error = error?.let { getString(it) }
        }
        viewModel.errorNombre.observe(this) { error ->
            etNombre.error = error?.let { getString(it) }
        }
        viewModel.errorApellido.observe(this) { error ->
            etApellido.error = error?.let { getString(it) }
        }
        viewModel.errorEmail.observe(this) { error ->
            etEmail.error = error?.let { getString(it) }
        }
        viewModel.errorContrasena.observe(this) { error ->
            etContrasena.error = error?.let { getString(it) }
        }
        viewModel.errorConfirmar.observe(this) { error ->
            etConfirmar.error = error?.let { getString(it) }
        }
        viewModel.registroCorrecto.observe(this) { correcto ->
            if (correcto) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        findViewById<Button>(R.id.btnRegistro).setOnClickListener {
            viewModel.registrar(
                etUsuario.text.toString(),
                etNombre.text.toString(),
                etApellido.text.toString(),
                etEmail.text.toString(),
                etContrasena.text.toString(),
                etConfirmar.text.toString()
            )
        }
        findViewById<Button>(R.id.btnVolverLogin).setOnClickListener {
            finish()
        }
    }
}