package com.javiergamez.catalogoui.views.secciones

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.javiergamez.catalogoui.views.R
import com.javiergamez.catalogoui.views.componentes.snackbar
import com.javiergamez.catalogoui.views.databinding.FragmentDetalleBinding
import com.javiergamez.catalogoui.views.datos.CatalogoViewModel

/** Detalle de un elemento de la lista vertical; recibe su id como argumento. */
class DetalleFragment : Fragment(R.layout.fragment_detalle) {

    private val vm: CatalogoViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentDetalleBinding.bind(view)
        val id = requireArguments().getInt("id")
        val elemento = vm.buscar(id)

        b.botonRegresar.setOnClickListener { findNavController().popBackStack() }

        if (elemento == null) {
            b.titulo.text = "El elemento ya no existe."
            b.avatar.isVisible = false
            b.botonEliminar.isVisible = false
            return
        }

        val lista = vm.elementos.value.orEmpty()
        b.avatar.text = elemento.titulo.take(1).uppercase()
        b.titulo.text = elemento.titulo
        b.descripcion.text = elemento.descripcion
        b.datos.text = buildString {
            appendLine("Identificador: ${elemento.id}")
            appendLine("Origen: ${if (elemento.deUsuario) "capturado en la Sección 1" else "lista predeterminada"}")
            append("Posición en la lista: ${lista.indexOf(elemento) + 1} de ${lista.size}")
        }
        b.botonEliminar.setOnClickListener {
            vm.eliminar(elemento.id)
            snackbar("Se eliminó “${elemento.titulo}”")
            findNavController().popBackStack()
        }
    }
}
