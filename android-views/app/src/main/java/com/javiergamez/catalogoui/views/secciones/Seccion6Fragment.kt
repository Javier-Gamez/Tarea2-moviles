package com.javiergamez.catalogoui.views.secciones

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import com.javiergamez.catalogoui.views.R
import com.javiergamez.catalogoui.views.componentes.dp
import com.javiergamez.catalogoui.views.componentes.mostrar
import com.javiergamez.catalogoui.views.databinding.FragmentSeccion6Binding
import com.javiergamez.catalogoui.views.navegacion.Seccion
import kotlin.math.roundToInt

class Seccion6Fragment : Fragment(R.layout.fragment_seccion6) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentSeccion6Binding.bind(view)
        b.encabezado.mostrar(Seccion.CONTENEDORES)

        configurarFila(b)
        configurarColumna(b)
        configurarCapas(b)
        configurarDesplazamiento(b)

        // Barra superior de ejemplo: cada acción muestra qué se pulsó.
        b.barraDemo.setNavigationOnClickListener { b.resultadoBarra.text = "Acción: Menú de navegación" }
        b.barraDemo.setOnMenuItemClickListener { item ->
            val nombre = if (item.itemId == R.id.demo_buscar || item.itemId == R.id.demo_favoritos) {
                item.title
            } else {
                "Menú: ${item.title}"
            }
            b.resultadoBarra.text = "Acción: $nombre"
            true
        }

        // Navegación inferior de ejemplo.
        b.navegacionInferior.setOnItemSelectedListener { item ->
            val (texto, icono) = when (item.itemId) {
                R.id.destino_explorar -> "Descubre contenido nuevo" to R.drawable.ic_explore
                R.id.destino_perfil -> "Tu información personal" to R.drawable.ic_person
                else -> "Contenido de la pantalla de inicio" to R.drawable.ic_home
            }
            b.contenidoNavegacion.text = texto
            b.contenidoNavegacion.setCompoundDrawablesRelativeWithIntrinsicBounds(0, icono, 0, 0)
            true
        }

        // Pesos dentro de una cadena de ConstraintLayout.
        b.deslizadorPeso.addOnChangeListener { _, valor, _ ->
            val peso = valor.roundToInt()
            b.bloquePesoA.updateLayoutParams<ConstraintLayout.LayoutParams> { horizontalWeight = peso.toFloat() }
            b.bloquePesoA.text = "A · $peso"
            val porcentaje = (peso * 100f / (peso + 1)).roundToInt()
            b.resultadoPesos.text = "A ocupa $porcentaje % del ancho y B el ${100 - porcentaje} %"
        }
    }

    private fun configurarFila(b: FragmentSeccion6Binding) {
        b.opcionesFila.addOnButtonCheckedListener { _, id, marcado ->
            if (!marcado) return@addOnButtonCheckedListener
            val repartir = id == R.id.fila_repartido
            b.demoFila.gravity = when (id) {
                R.id.fila_centro -> Gravity.CENTER_HORIZONTAL
                R.id.fila_final -> Gravity.END
                else -> Gravity.START
            }
            // "Repartir" asigna el mismo peso a cada bloque para que ocupen todo el ancho.
            b.demoFila.children.forEach { bloque ->
                bloque.updateLayoutParams<LinearLayout.LayoutParams> {
                    width = if (repartir) 0 else bloque.dp(56)
                    weight = if (repartir) 1f else 0f
                }
            }
        }
    }

    private fun configurarColumna(b: FragmentSeccion6Binding) {
        b.opcionesColumna.addOnButtonCheckedListener { _, id, marcado ->
            if (!marcado) return@addOnButtonCheckedListener
            b.demoColumna.gravity = when (id) {
                R.id.columna_centro -> Gravity.CENTER_HORIZONTAL
                R.id.columna_derecha -> Gravity.END
                else -> Gravity.START
            }
        }
    }

    private fun configurarCapas(b: FragmentSeccion6Binding) {
        b.opcionesCapa.addOnButtonCheckedListener { _, id, marcado ->
            if (!marcado) return@addOnButtonCheckedListener
            b.etiquetaCapa.updateLayoutParams<FrameLayout.LayoutParams> {
                gravity = when (id) {
                    R.id.capa_arriba -> Gravity.TOP or Gravity.START
                    R.id.capa_centro -> Gravity.CENTER
                    else -> Gravity.BOTTOM or Gravity.END
                }
            }
        }
    }

    private fun configurarDesplazamiento(b: FragmentSeccion6Binding) {
        repeat(20) { i ->
            b.parrafos.addView(TextView(requireContext()).apply {
                text = "Párrafo ${i + 1}: contenido que requiere desplazamiento."
                setPadding(0, 0, 0, dp(8))
            })
        }
        val desplazable = b.contenedorDesplazable
        fun alFinal() = !desplazable.canScrollVertically(1)
        desplazable.setOnScrollChangeListener { _: View, _: Int, y: Int, _: Int, _: Int ->
            b.textoDesplazamiento.text = "Desplazamiento: $y px"
            b.botonDesplazar.text = if (alFinal()) "Ir al inicio" else "Ir al final"
        }
        b.botonDesplazar.setOnClickListener {
            if (alFinal()) desplazable.smoothScrollTo(0, 0)
            else desplazable.smoothScrollTo(0, b.parrafos.height)
        }
    }
}
