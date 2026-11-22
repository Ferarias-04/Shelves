package com.shelves.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

/**
 * Pantalla de Preferencias. Se abre desde el menú y se muestra en el contenedor central,
 * así que el botón "atrás" vuelve a la pestaña en la que estaba el usuario.
 *
 * De momento solo funciona "Cerrar sesión", que vuelve al Login y borra la pila de
 * pantallas para que no se pueda volver a entrar con "atrás".
 */
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