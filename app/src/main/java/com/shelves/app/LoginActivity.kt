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
 * Pantalla de inicio de sesión. Se abre después del Splash.
 *
 * Sigue el patrón MVVM: esta Activity es la Vista. No decide si los datos son correctos;
 * le pasa el texto a LoginViewModel y observa sus LiveData para mostrar los errores
 * o para entrar en la app. También da acceso a la pantalla de registro.
 *
 * @see LoginViewModel
 * @see RegistreActivity
 */
class LoginActivity : AppCompatActivity() {

    /**
     * ViewModel de esta pantalla.
     * by viewModels() lo crea la primera vez y, si la Activity se recrea
     * (por ejemplo, al girar el móvil), devuelve el mismo objeto.
     */
    private val viewModel: LoginViewModel by viewModels()

    /**
     * Prepara la pantalla.
     *
     * Primero se suscribe a los LiveData del ViewModel con observe(): cada vez que
     * el ViewModel cambia un valor, se ejecuta el bloque correspondiente.
     * <ul>
     *   <li>errorUsuario y errorContrasena: muestran el mensaje de error en su campo,
     *       o lo quitan si el valor es null.</li>
     *   <li>loginCorrecto: si es true, abre MainActivity y cierra el login para que
     *       el botón "atrás" no vuelva aquí.</li>
     * </ul>
     * Después configura los botones: "Entrar" envía los datos al ViewModel para que
     * los valide, y "Sign Up" abre la pantalla de registro.
     *
     * @param savedInstanceState estado guardado si la Activity se está recreando, o null.
     */
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

        viewModel.errorUsuario.observe(this) { error ->
            etUsuario.error = error?.let { getString(it) }
        }
        viewModel.errorContrasena.observe(this) { error ->
            etContrasena.error = error?.let { getString(it) }
        }
        viewModel.loginCorrecto.observe(this) { correcto ->
            if (correcto) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            viewModel.login(etUsuario.text.toString(), etContrasena.text.toString())
        }
        findViewById<Button>(R.id.btnSignUp).setOnClickListener {
            startActivity(Intent(this, RegistreActivity::class.java))
        }
    }
}