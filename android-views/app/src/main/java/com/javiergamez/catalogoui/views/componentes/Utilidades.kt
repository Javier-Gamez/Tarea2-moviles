package com.javiergamez.catalogoui.views.componentes

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.javiergamez.catalogoui.views.R
import com.javiergamez.catalogoui.views.databinding.EncabezadoSeccionBinding
import com.javiergamez.catalogoui.views.navegacion.Seccion

/** Rellena el encabezado común de cada sección. */
fun EncabezadoSeccionBinding.mostrar(seccion: Seccion) {
    icono.setImageResource(seccion.icono)
    numero.text = "Sección ${seccion.numero}"
    titulo.text = seccion.titulo
    resumen.text = seccion.resumen
}

/** Mensaje emergente breve del sistema. */
fun Fragment.toast(mensaje: String) {
    Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
}

/** Mensaje en la parte inferior de la pantalla, con una acción opcional. */
fun Fragment.snackbar(mensaje: String, accion: String? = null, alAccion: () -> Unit = {}) {
    val raiz = requireActivity().findViewById<View>(R.id.coordinador)
    val duracion = if (accion == null) Snackbar.LENGTH_SHORT else Snackbar.LENGTH_LONG
    Snackbar.make(raiz, mensaje, duracion).apply {
        if (accion != null) setAction(accion) { alAccion() }
    }.show()
}

/** Convierte dp a píxeles. */
fun View.dp(valor: Int): Int = (valor * resources.displayMetrics.density).toInt()
