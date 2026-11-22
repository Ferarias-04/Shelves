package com.shelves.app

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * Pantalla inicial de la app (es la activity LAUNCHER del manifest).
 *
 * Muestra el logo de Shelves con una animación de entrada (fundido y escala, definida en
 * `res/anim/splash_logo.xml`); al terminar aparece el lema y se abre el Login.
 * Si el usuario tiene desactivadas las animaciones del sistema, se salta la animación.
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logo = findViewById<ImageView>(R.id.imgSplashLogo)
        val lema = findViewById<TextView>(R.id.tvSplashLema)

        // Accesibilidad: si el usuario ha desactivado las animaciones del sistema,
        // no animamos y pasamos directamente al login.
        if (animacionesDesactivadas()) {
            irAlLogin()
            return
        }

        val anim = AnimationUtils.loadAnimation(this, R.anim.splash_logo)
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                // Cuando acaba el logo, aparece el lema y luego cambiamos de pantalla
                lema.animate()
                    .alpha(1f)
                    .setDuration(500)
                    .withEndAction { lema.postDelayed({ irAlLogin() }, 700) }
            }
        })
        logo.startAnimation(anim)
    }

    /**
     * Abre el Login y cierra el Splash con finish(), para que al pulsar "atrás"
     * no se vuelva a ver la animación.
     */
    private fun irAlLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish() // para que al pulsar "atrás" no se vuelva al splash
    }

    /**
     * Comprueba si el usuario ha quitado las animaciones en los ajustes del móvil
     * (opción de accesibilidad pensada para personas a las que el movimiento les molesta o marea).
     *
     * @return true si las animaciones están desactivadas.
     */
    private fun animacionesDesactivadas(): Boolean =
        Settings.Global.getFloat(contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f
}