package com.shelves.app
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
//pestaña inicio: racha, lectura actual, frase del mes y libros más leídos
class HomeFragment : Fragment (R.layout.fragment_home){


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PortadaAdapter { libro ->
            Log.d("Inicio", "Portada pulsada: ${libro.titulo}")
            Toast.makeText(
                requireContext(),
                getString(R.string.top_desc, libro.titulo, libro.autor),
                Toast.LENGTH_SHORT
            ).show()
        }
        view.findViewById<RecyclerView>(R.id.rvTopLibros).adapter = adapter

        view.findViewById<ChipGroup>(R.id.chipsGeneros).setOnCheckedStateChangeListener { _, ids ->
            val genero = when (ids.firstOrNull()) {
                R.id.chipsTerror -> Genero.TERROR
                R.id.chipsRomance -> Genero.ROMANCE
                R.id.chipsFantasia -> Genero.FANTASIA
                R.id.chipsDistopia -> Genero.DISTOPIA
                else -> Genero.GENERAL
            }
            adapter.mostrar(TopLibros.porGenero(genero))
        }

        adapter.mostrar(TopLibros.porGenero(Genero.GENERAL))
    }
}