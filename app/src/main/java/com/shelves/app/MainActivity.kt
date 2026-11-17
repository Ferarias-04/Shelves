package com.shelves.app

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment


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

        // Solo la primera vez: al girar la pantalla, Android ya recupera el fragment que había
        if (savedInstanceState == null) {
            navegarA(btnHome, HomeFragment())
        }
    }

    /** Cambia el fragment del contenedor central y actualiza la barra inferior. */
    private fun navegarA(boton: View, fragment: Fragment) {
        if (boton.isSelected) return // ya estamos en esa pestaña

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.fragment_container, fragment)
            .commit()

        marcarActivo(boton)
    }

    /** Pone el icono "_active" en el botón pulsado y el normal en los demás. */
    private fun marcarActivo(boton: View) {
        icHome.setImageResource(if (boton == btnHome) R.drawable.home_active else R.drawable.home)
        icLibrary.setImageResource(if (boton == btnLibrary) R.drawable.books_active else R.drawable.books)
        icProfile.setImageResource(if (boton == btnProfile) R.drawable.user_active else R.drawable.user)

        // Accesibilidad: con isSelected, TalkBack dice "seleccionado" en la pestaña actual
        btnHome.isSelected = boton == btnHome
        btnLibrary.isSelected = boton == btnLibrary
        btnProfile.isSelected = boton == btnProfile
    }
}