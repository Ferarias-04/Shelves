package com.shelves.app

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment

/**
 * Menú principal de la app. No es un Navigation Drawer, sino un fragment propio que
 * [MainActivity] coloca en `overlay_container`, encima de todas las pantallas, al pulsar
 * el botón hamburguesa.
 *
 * Desde aquí se abre Preferencias o Ayuda (con [MainActivity.abrirSeccion]), se cambia
 * entre modo claro y oscuro, y se accede a los enlaces de contacto (de momento solo
 * muestran un aviso). El menú se cierra con su botón o con el "atrás" del móvil,
 * porque se guarda en el back stack.
 */
class MenuFragment : Fragment(R.layout.fragment_menu) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val main = requireActivity() as MainActivity

        view.findViewById<View>(R.id.btnCloseMenu).setOnClickListener { main.cerrarMenu() }

        view.findViewById<View>(R.id.btnPreferences).setOnClickListener {
            main.abrirSeccion(PreferenciasFragment())
        }
        view.findViewById<View>(R.id.btnHelp).setOnClickListener {
            main.abrirSeccion(AyudaFragment())
        }

        view.findViewById<View>(R.id.btnDarkMode).setOnClickListener { cambiarModoOscuro() }

        // Contacto: de momento solo avisamos
        val proximamente = View.OnClickListener {
            Log.d("MenuFragment", "Pulsado contacto: ${resources.getResourceEntryName(it.id)}")
            Toast.makeText(requireContext(), R.string.menu_proximamente, Toast.LENGTH_SHORT).show()
        }
        listOf(R.id.btnInstagram, R.id.btnThreads, R.id.btnEmail).forEach {
            view.findViewById<View>(it).setOnClickListener(proximamente)
        }
    }

    /** Alterna entre modo claro y oscuro. Android vuelve a crear la pantalla con los colores nuevos. */
    private fun cambiarModoOscuro() {
        val modoActual = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val esOscuro = modoActual == Configuration.UI_MODE_NIGHT_YES
        AppCompatDelegate.setDefaultNightMode(
            if (esOscuro) AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES
        )
    }
}