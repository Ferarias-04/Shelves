package com.shelves.app

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.core.view.isVisible
/**
 * Pantalla principal: sirve de marco para toda la app.
 *
 * Contiene la cabecera, la barra de navegación inferior y dos contenedores de fragments:
 * uno central (Inicio, Biblioteca, Perfil y secciones) y otro por encima de todo para el menú.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var btnHome: View
    private lateinit var btnLibrary: View
    private lateinit var btnProfile: View
    private lateinit var icHome: ImageView
    private lateinit var icLibrary: ImageView
    private lateinit var icProfile: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnHome = findViewById(R.id.btnHome)
        btnLibrary = findViewById(R.id.btnLibrary)
        btnProfile = findViewById(R.id.btnProfile)
        icHome = findViewById(R.id.icHome)
        icLibrary = findViewById(R.id.icLibrary)
        icProfile = findViewById(R.id.icProfile)

        // Barra inferior: cada botón carga su fragment y se marca como activo
        btnHome.setOnClickListener { navegarA(btnHome, HomeFragment()) }
        btnLibrary.setOnClickListener { navegarA(btnLibrary, BibliotecaFragment()) }
        btnProfile.setOnClickListener { navegarA(btnProfile, PerfilFragment()) }

        // Botón hamburguesa
        findViewById<View>(R.id.btnMenu).setOnClickListener { abrirMenu() }

        // Solo la primera vez: al girar la pantalla, Android ya recupera el fragment que había
        if (savedInstanceState == null) {
            navegarA(btnHome, HomeFragment())
        }
    }

    // Barra inferior

    /**
     * Cambia la pestaña principal (Inicio, Biblioteca o Perfil).
     * Si había una pantalla encima (Preferencias o Ayuda), la quita de la pila antes de cambiar.
     *
     * @param boton botón de la barra inferior que se ha pulsado.
     * @param fragment fragment que se carga en el contenedor central.
     */
    private fun navegarA(boton: View, fragment: Fragment) {
        val hayPantallaEncima = supportFragmentManager.backStackEntryCount > 0
        if (boton.isSelected && !hayPantallaEncima) return // ya estamos ahí

        // Si estábamos en Preferencias o Ayuda, las quitamos de la pila
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.fragment_container, fragment)
            .commit()

        marcarActivo(boton)
    }

    /**
     * Marca como activo el botón pulsado: cambia su icono al relleno y avisa a TalkBack
     * de que está seleccionado.
     *
     * @param boton botón de la barra inferior que pasa a estar activo.
     */
    private fun marcarActivo(boton: View) {
        icHome.setImageResource(if (boton == btnHome) R.drawable.home_active else R.drawable.home)
        icLibrary.setImageResource(if (boton == btnLibrary) R.drawable.books_active else R.drawable.books)
        icProfile.setImageResource(if (boton == btnProfile) R.drawable.user_active else R.drawable.user)

        // Accesibilidad: con isSelected, TalkBack dice "seleccionado" en la pestaña actual
        btnHome.isSelected = boton == btnHome
        btnLibrary.isSelected = boton == btnLibrary
        btnProfile.isSelected = boton == btnProfile
    }

    // Menú hamburguesa

    /** Muestra el menú por encima de todo, deslizándose desde la izquierda. */
    private fun abrirMenu() {
        if (supportFragmentManager.findFragmentById(R.id.overlay_container) != null) return

        supportFragmentManager.beginTransaction()
            // entrar, salir, volver a entrar, salir al pulsar atrás
            .setCustomAnimations(R.anim.menu_entrar, 0, 0, R.anim.menu_salir)
            .add(R.id.overlay_container, MenuFragment())
            .addToBackStack(TAG_MENU) // así el botón "atrás" del móvil también lo cierra
            .commit()
    }

    fun cerrarMenu() {
        supportFragmentManager.popBackStack(TAG_MENU, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    /**
     * Cierra el menú y abre una sección (Preferencias o Ayuda) en el contenedor central.
     * Se añade a la pila para que "atrás" vuelva a la pestaña en la que se estaba.
     *
     * @param fragment sección que se muestra.
     */
    fun abrirSeccion(fragment: Fragment) {
        // Quita el menú (y otra sección si ya había una abierta)
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                android.R.anim.fade_in, android.R.anim.fade_out,
                android.R.anim.fade_in, android.R.anim.fade_out
            )
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null) // "atrás" vuelve a la pestaña en la que estabas
            .commit()
    }

    fun mostrarBarraNavegacion(visible: Boolean){
        findViewById<View>(R.id.bottom_bar).isVisible = visible
    }
    fun menuAbierto(): Boolean =
        supportFragmentManager.findFragmentById(R.id.overlay_container) != null

    companion object {
        private const val TAG_MENU = "menu"
    }
}