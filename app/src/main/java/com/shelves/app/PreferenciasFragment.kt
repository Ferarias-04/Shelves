package com.shelves.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

/** Pantalla de Preferencias (se abre desde el menú). */
class PreferenciasFragment : Fragment(R.layout.fragment_preferencias) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Cerrar sesión: volvemos al login y borramos la pila de pantallas
        view.findViewById<View>(R.id.btnCerrarSesion).setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}