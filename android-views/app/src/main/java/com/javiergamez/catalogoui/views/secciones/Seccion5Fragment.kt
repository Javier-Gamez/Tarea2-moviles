package com.javiergamez.catalogoui.views.secciones

import android.animation.ValueAnimator
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.OptIn
import androidx.core.animation.doOnEnd
import androidx.core.text.inSpans
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil3.load
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.javiergamez.catalogoui.views.R
import com.javiergamez.catalogoui.views.componentes.mostrar
import com.javiergamez.catalogoui.views.componentes.snackbar
import com.javiergamez.catalogoui.views.componentes.toast
import com.javiergamez.catalogoui.views.databinding.FragmentSeccion5Binding
import com.javiergamez.catalogoui.views.databinding.HojaInferiorBinding
import com.javiergamez.catalogoui.views.datos.CatalogoViewModel
import com.javiergamez.catalogoui.views.navegacion.Seccion

const val URL_IMAGEN = "https://picsum.photos/id/1018/800/500"

class Seccion5Fragment : Fragment(R.layout.fragment_seccion5) {

    private val vm: CatalogoViewModel by activityViewModels()
    private var animacion: ValueAnimator? = null
    private var notificaciones = 3

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentSeccion5Binding.bind(view)
        b.encabezado.mostrar(Seccion.INFORMACION)

        configurarTextos(b)
        configurarImagenes(b)
        configurarProgreso(b)

        // Toast y snackbar.
        b.botonToast.setOnClickListener { toast("Esto es un toast") }
        b.botonSnackbar.setOnClickListener {
            b.resultadoAvisos.text = "Estado: archivada (pulsa Deshacer en el snackbar)"
            snackbar("Conversación archivada", "Deshacer") { b.resultadoAvisos.text = "Estado: en la bandeja" }
        }

        // Diálogo de confirmación.
        b.botonDialogo.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setIcon(R.drawable.ic_delete)
                .setTitle("¿Eliminar archivo?")
                .setMessage("El archivo “reporte.pdf” se eliminará de forma permanente. Esta acción no se puede deshacer.")
                .setNegativeButton("Cancelar") { _, _ -> b.resultadoDialogo.text = "Cancelaste la eliminación." }
                .setPositiveButton("Eliminar") { _, _ -> b.resultadoDialogo.text = "Confirmaste: archivo eliminado." }
                .setOnCancelListener { b.resultadoDialogo.text = "Diálogo cerrado sin elegir." }
                .show()
        }

        // Hoja inferior.
        b.botonHoja.setOnClickListener {
            val hoja = BottomSheetDialog(requireContext())
            val contenido = HojaInferiorBinding.inflate(layoutInflater)
            listOf(contenido.opcionCompartir, contenido.opcionCopiar, contenido.opcionDescargar).forEach { opcion ->
                opcion.setOnClickListener {
                    b.resultadoHoja.text = "Elegiste: ${opcion.text}"
                    hoja.dismiss()
                }
            }
            hoja.setContentView(contenido.root)
            hoja.show()
        }

        configurarDistintivo(b)
    }

    private fun configurarTextos(b: FragmentSeccion5Binding) {
        val primario = MaterialColors.getColor(b.root, androidx.appcompat.R.attr.colorPrimary)
        b.textoEnriquecido.text = SpannableStringBuilder()
            .append("Texto con ")
            .inSpans(StyleSpan(Typeface.BOLD)) { append("negrita") }
            .append(", ")
            .inSpans(StyleSpan(Typeface.ITALIC)) { append("cursiva") }
            .append(", ")
            .inSpans(ForegroundColorSpan(primario)) { append("color") }
            .append(", ")
            .inSpans(StrikethroughSpan()) { append("tachado") }
            .append(" y ")
            .inSpans(TypefaceSpan("monospace")) { append("monoespaciado") }
            .append(".")

        // Énfasis de la frase de ejemplo.
        fun aplicarEnfasis() {
            val estilo = when {
                b.chipNegrita.isChecked && b.chipCursiva.isChecked -> Typeface.BOLD_ITALIC
                b.chipNegrita.isChecked -> Typeface.BOLD
                b.chipCursiva.isChecked -> Typeface.ITALIC
                else -> Typeface.NORMAL
            }
            b.fraseEjemplo.setTypeface(Typeface.create(Typeface.DEFAULT, estilo))
            b.fraseEjemplo.paintFlags = if (b.chipSubrayado.isChecked) {
                b.fraseEjemplo.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            } else {
                b.fraseEjemplo.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
            }
        }
        listOf(b.chipNegrita, b.chipCursiva, b.chipSubrayado).forEach {
            it.setOnCheckedChangeListener { _, _ -> aplicarEnfasis() }
        }

        // Conexión con la Sección 3: el tamaño elegido escala todos los ejemplos.
        val textos = b.muestrasTexto.children.filterIsInstance<TextView>().toList() + b.fraseEjemplo
        val tamanosBase = textos.associateWith { it.textSize }
        vm.tamanoTexto.observe(viewLifecycleOwner) { tamano ->
            textos.forEach { it.setTextSize(TypedValue.COMPLEX_UNIT_PX, tamanosBase.getValue(it) * tamano.escala) }
            b.resultadoTamano.text = "Tamaño actual: ${tamano.etiqueta}"
        }
        b.botonIrSeccion3.setOnClickListener { findNavController().navigate(Seccion.SELECCION.destino) }
    }

    private fun configurarImagenes(b: FragmentSeccion5Binding) {
        b.imagenRed.load(URL_IMAGEN) {
            listener(
                onSuccess = { _, _ -> b.cargaImagen.isVisible = false },
                onError = { _, _ ->
                    b.cargaImagen.isVisible = false
                    b.errorImagen.isVisible = true
                },
            )
        }
        val modos = mapOf(
            R.id.escala_recortar to ("Recortar" to ImageView.ScaleType.CENTER_CROP),
            R.id.escala_ajustar to ("Ajustar" to ImageView.ScaleType.FIT_CENTER),
            R.id.escala_estirar to ("Estirar" to ImageView.ScaleType.FIT_XY),
            R.id.escala_original to ("Original" to ImageView.ScaleType.CENTER),
        )
        b.grupoEscalado.addOnButtonCheckedListener { _, id, marcado ->
            if (!marcado) return@addOnButtonCheckedListener
            val (nombre, escala) = modos.getValue(id)
            b.imagenLocal.scaleType = escala
            b.imagenRed.scaleType = escala
            b.resultadoEscalado.text = "Modo de escalado: $nombre"
        }
    }

    private fun configurarProgreso(b: FragmentSeccion5Binding) {
        fun mostrar(valor: Int) {
            b.linealDeterminado.setProgressCompat(valor, false)
            b.circularDeterminado.setProgressCompat(valor, false)
            b.textoProgreso.text = "Determinado: $valor %"
        }
        b.botonIniciar.setOnClickListener {
            animacion?.cancel()
            animacion = ValueAnimator.ofInt(0, 100).apply {
                duration = 3000
                addUpdateListener { mostrar(it.animatedValue as Int) }
                doOnEnd {
                    b.botonIniciar.isEnabled = true
                    if (animatedValue == 100 && isAdded) snackbar("Descarga simulada completada")
                }
                b.botonIniciar.isEnabled = false
                start()
            }
        }
        b.botonReiniciar.setOnClickListener {
            animacion?.cancel()
            mostrar(0)
        }
        b.interruptorIndeterminados.setOnCheckedChangeListener { _, activo -> b.indeterminados.isVisible = activo }
    }

    @OptIn(ExperimentalBadgeUtils::class)
    private fun configurarDistintivo(b: FragmentSeccion5Binding) {
        val distintivo = BadgeDrawable.create(requireContext())
        fun actualizar() {
            distintivo.isVisible = notificaciones > 0
            distintivo.number = notificaciones
            b.textoNotificaciones.text = if (notificaciones == 0) "Estás al día" else "Tienes $notificaciones sin leer"
        }
        // El distintivo se ancla al ícono cuando ya tiene tamaño en pantalla.
        b.iconoNotificaciones.post {
            BadgeUtils.attachBadgeDrawable(distintivo, b.iconoNotificaciones)
        }
        b.botonLeidas.setOnClickListener { notificaciones = 0; actualizar() }
        b.botonRecibir.setOnClickListener { notificaciones++; actualizar() }
        actualizar()
    }

    override fun onDestroyView() {
        animacion?.cancel()
        animacion = null
        super.onDestroyView()
    }
}
