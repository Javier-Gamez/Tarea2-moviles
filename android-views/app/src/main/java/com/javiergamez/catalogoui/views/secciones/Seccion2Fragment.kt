package com.javiergamez.catalogoui.views.secciones

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.R as MaterialR
import com.google.android.material.color.MaterialColors
import com.google.android.material.progressindicator.CircularProgressIndicatorSpec
import com.google.android.material.progressindicator.IndeterminateDrawable
import com.javiergamez.catalogoui.views.R
import com.javiergamez.catalogoui.views.componentes.mostrar
import com.javiergamez.catalogoui.views.componentes.snackbar
import com.javiergamez.catalogoui.views.componentes.toast
import com.javiergamez.catalogoui.views.databinding.FragmentSeccion2Binding
import com.javiergamez.catalogoui.views.navegacion.Seccion
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Seccion2Fragment : Fragment(R.layout.fragment_seccion2) {

    private var relleno = 0
    private var contorno = 0
    private var texto = 0
    private var creados = 0
    private var descargas = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentSeccion2Binding.bind(view)
        b.encabezado.mostrar(Seccion.BOTONES)

        // Relleno, contorno y texto.
        fun actualizarBasicos() {
            b.resultadoBasicos.text = "Pulsaciones → relleno: $relleno · contorno: $contorno · texto: $texto"
        }
        b.botonRelleno.setOnClickListener { relleno++; actualizarBasicos() }
        b.botonContorno.setOnClickListener { contorno++; actualizarBasicos() }
        b.botonTexto.setOnClickListener { texto++; actualizarBasicos() }
        actualizarBasicos()

        // Botones con ícono.
        var favorito = false
        b.botonFavorito.setOnClickListener {
            favorito = !favorito
            b.botonFavorito.setIconResource(if (favorito) R.drawable.ic_favorite else R.drawable.ic_favorite_border)
            b.botonFavorito.iconTint = if (favorito) {
                android.content.res.ColorStateList.valueOf(MaterialColors.getColor(b.botonFavorito, androidx.appcompat.R.attr.colorError))
            } else {
                android.content.res.ColorStateList.valueOf(MaterialColors.getColor(b.botonFavorito, MaterialR.attr.colorOnSurfaceVariant))
            }
            b.resultadoIconos.text = if (favorito) "Agregado a favoritos" else "Quitado de favoritos"
        }
        b.botonCompartir.setOnClickListener {
            b.resultadoIconos.text = "Compartir"
            toast("Compartiendo contenido…")
        }
        b.botonEnviar.setOnClickListener {
            b.resultadoIconos.text = "Mensaje enviado"
            snackbar("Mensaje enviado correctamente")
        }

        // FAB normal y extendido.
        b.fab.setOnClickListener {
            creados++
            b.textoCreados.text = "Elementos creados: $creados"
        }
        b.fabExtendido.setOnClickListener { snackbar("Abriendo el editor…") }
        b.interruptorExtendido.setOnCheckedChangeListener { _, activo ->
            if (activo) b.fabExtendido.extend() else b.fabExtendido.shrink()
        }

        // Selector segmentado y botón de alternancia.
        b.grupoVista.addOnButtonCheckedListener { _, id, marcado ->
            if (!marcado) return@addOnButtonCheckedListener
            val vista = when (id) {
                R.id.vista_semana -> "Semana"
                R.id.vista_mes -> "Mes"
                else -> "Día"
            }
            b.resultadoVista.text = "Vista seleccionada: $vista"
        }
        b.botonGuardar.addOnCheckedChangeListener { boton, marcado ->
            boton.setIconResource(if (marcado) R.drawable.ic_bookmark else R.drawable.ic_bookmark_border)
            b.textoGuardado.text = if (marcado) "Guardado en marcadores" else "Sin guardar"
        }

        // Deshabilitado y en carga.
        b.interruptorHabilitar.setOnCheckedChangeListener { _, activo ->
            b.botonDeshabilitado.isEnabled = activo
            b.botonDeshabilitado.text = if (activo) "Habilitado" else "Deshabilitado"
        }
        b.botonDeshabilitado.setOnClickListener { toast("¡El botón está habilitado!") }

        val especificacion = CircularProgressIndicatorSpec(
            requireContext(), null, 0, MaterialR.style.Widget_Material3_CircularProgressIndicator_ExtraSmall,
        )
        val indicador = IndeterminateDrawable.createCircularDrawable(requireContext(), especificacion)
        b.botonDescargar.setOnClickListener {
            b.botonDescargar.isEnabled = false
            b.botonDescargar.icon = indicador
            b.botonDescargar.text = "Cargando…"
            viewLifecycleOwner.lifecycleScope.launch {
                delay(2000)
                descargas++
                b.botonDescargar.isEnabled = true
                b.botonDescargar.setIconResource(R.drawable.ic_cloud_download)
                b.botonDescargar.text = "Descargar"
                b.resultadoDescargas.text = "Descargas completadas: $descargas"
                snackbar("Operación completada")
            }
        }
        b.textoCreados.text = "Elementos creados: $creados"
        b.resultadoDescargas.text = "Descargas completadas: $descargas"
    }
}
