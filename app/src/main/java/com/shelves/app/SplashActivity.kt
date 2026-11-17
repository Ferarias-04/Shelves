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
 * Pantalla inicial: anima el logo de Shelves y después abre el Login.
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

    private fun irAlLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish() // para que al pulsar "atrás" no se vuelva al splash
    }

    private fun animacionesDesactivadas(): Boolean =
        Settings.Global.getFloat(contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f
}