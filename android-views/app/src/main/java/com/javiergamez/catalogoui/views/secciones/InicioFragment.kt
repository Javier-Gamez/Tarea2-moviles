package com.javiergamez.catalogoui.views.secciones

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.javiergamez.catalogoui.views.R
import com.javiergamez.catalogoui.views.databinding.FragmentInicioBinding
import com.javiergamez.catalogoui.views.databinding.ItemSeccionBinding
import com.javiergamez.catalogoui.views.datos.CatalogoViewModel
import com.javiergamez.catalogoui.views.navegacion.Seccion

/** Pantalla principal: presentación de la app y acceso a las seis secciones. */
class InicioFragment : Fragment(R.layout.fragment_inicio) {

    private val vm: CatalogoViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentInicioBinding.bind(view)

        Seccion.entries.forEach { seccion ->
            val tarjeta = ItemSeccionBinding.inflate(layoutInflater, b.listaSecciones, true)
            tarjeta.icono.setImageResource(seccion.icono)
            tarjeta.numero.text = "Sección ${seccion.numero}"
            tarjeta.titulo.text = seccion.titulo
            tarjeta.resumen.text = seccion.resumen
            tarjeta.root.setOnClickListener { findNavController().navigate(seccion.destino) }
        }

        vm.elementos.observe(viewLifecycleOwner) { lista ->
            b.conexion1.text = "• Sección 1 → 4: el texto capturado se agrega a la lista vertical " +
                "(actualmente hay ${lista.size} elementos)."
        }
        vm.tamanoTexto.observe(viewLifecycleOwner) { tamano ->
            b.conexion2.text = "• Sección 3 → 5: el tamaño de texto elegido (${tamano.etiqueta}) " +
                "cambia los ejemplos de tipografía."
        }
    }
}
