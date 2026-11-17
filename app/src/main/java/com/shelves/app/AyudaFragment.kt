package com.shelves.app

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

/** Pantalla de Ayuda (se abre desde el menú). */
class AyudaFragment : Fragment(R.layout.fragment_ayuda) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // "Entendido" vuelve a la pantalla de antes, igual que el botón atrás
        view.findViewById<View>(R.id.btnAyudaEntendido).setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}