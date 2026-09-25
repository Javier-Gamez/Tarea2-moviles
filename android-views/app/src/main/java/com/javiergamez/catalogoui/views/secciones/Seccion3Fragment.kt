package com.javiergamez.catalogoui.views.secciones

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.javiergamez.catalogoui.views.R
import com.javiergamez.catalogoui.views.componentes.mostrar
import com.javiergamez.catalogoui.views.databinding.FragmentSeccion3Binding
import com.javiergamez.catalogoui.views.datos.CatalogoViewModel
import com.javiergamez.catalogoui.views.datos.TamanoTexto
import com.javiergamez.catalogoui.views.navegacion.Seccion
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt

class Seccion3Fragment : Fragment(R.layout.fragment_seccion3) {

    private val vm: CatalogoViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentSeccion3Binding.bind(view)
        b.encabezado.mostrar(Seccion.SELECCION)

        configurarCasillas(b)

        // Botones de opción conectados con la Sección 5.
        val radios = mapOf(
            TamanoTexto.PEQUENO to R.id.tamano_pequeno,
            TamanoTexto.MEDIANO to R.id.tamano_mediano,
            TamanoTexto.GRANDE to R.id.tamano_grande,
        )
        val tamanoBase = b.vistaPrevia.textSize
        vm.tamanoTexto.observe(viewLifecycleOwner) { tamano ->
            if (b.grupoTamano.checkedRadioButtonId != radios[tamano]) b.grupoTamano.check(radios.getValue(tamano))
            b.vistaPrevia.setTextSize(TypedValue.COMPLEX_UNIT_PX, tamanoBase * tamano.escala)
            b.resultadoTamano.text = "Tamaño elegido: ${tamano.etiqueta}. Revisa la Sección 5."
        }
        b.grupoTamano.setOnCheckedChangeListener { _, id ->
            radios.entries.find { it.value == id }?.let { vm.elegirTamano(it.key) }
        }

        // Interruptores.
        fun actualizarInterruptores() {
            b.resultadoInterruptor.text = if (b.interruptorAvion.isChecked) {
                "Modo avión activado: conexiones desactivadas"
            } else {
                "Wi-Fi ${if (b.interruptorWifi.isChecked) "encendido" else "apagado"}"
            }
        }
        b.interruptorAvion.setOnCheckedChangeListener { _, activo ->
            if (activo) b.interruptorWifi.isChecked = false
            b.interruptorWifi.isEnabled = !activo
            actualizarInterruptores()
        }
        b.interruptorWifi.setOnCheckedChangeListener { _, _ -> actualizarInterruptores() }

        // Deslizadores.
        b.deslizadorVolumen.addOnChangeListener { _, valor, _ ->
            b.textoVolumen.text = "Volumen: ${valor.roundToInt()} %"
        }
        b.deslizadorPrecio.addOnChangeListener { deslizador, _, _ ->
            val (desde, hasta) = deslizador.values
            b.textoPrecio.text = "Precio: $${desde.roundToInt()} – $${hasta.roundToInt()} MXN"
        }

        // Lista desplegable.
        b.listaLenguajes.setOnItemClickListener { parent, _, posicion, _ ->
            b.resultadoLenguaje.text = "Elegiste: ${parent.getItemAtPosition(posicion)}"
        }

        // Selectores de fecha y hora.
        val formato = SimpleDateFormat("d 'de' MMMM 'de' yyyy", Locale.forLanguageTag("es-MX")).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        b.botonFecha.setOnClickListener {
            val selector = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecciona una fecha")
                .setPositiveButtonText("Aceptar")
                .setNegativeButtonText("Cancelar")
                .build()
            selector.addOnPositiveButtonClickListener { milis ->
                b.resultadoFecha.text = "Fecha: ${formato.format(Date(milis))}"
            }
            selector.show(childFragmentManager, "fecha")
        }
        b.botonHora.setOnClickListener {
            val selector = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("Selecciona la hora")
                .setPositiveButtonText("Aceptar")
                .setNegativeButtonText("Cancelar")
                .build()
            selector.addOnPositiveButtonClickListener {
                b.resultadoHora.text = "Hora: %02d:%02d".format(selector.hour, selector.minute)
            }
            selector.show(childFragmentManager, "hora")
        }

        // Chips de filtro creados a partir del arreglo de recursos.
        val intereses = resources.getStringArray(R.array.intereses)
        fun actualizarChips() {
            val activos = b.grupoChips.checkedChipIds.map { b.grupoChips.findViewById<Chip>(it).text }
            b.resultadoChips.text = if (activos.isEmpty()) "Sin filtros activos"
            else "Filtros activos (${activos.size}): ${activos.joinToString()}"
        }
        intereses.forEach { interes ->
            val chip = layoutInflater.inflate(R.layout.chip_filtro, b.grupoChips, false) as Chip
            chip.text = interes
            chip.id = View.generateViewId()
            chip.isChecked = interes == "Tecnología"
            b.grupoChips.addView(chip)
        }
        b.grupoChips.setOnCheckedStateChangeListener { _, _ -> actualizarChips() }
        actualizarChips()
    }

    private fun configurarCasillas(b: FragmentSeccion3Binding) {
        val hijas = listOf(b.casillaQueso, b.casillaJamon, b.casillaChampinones)
        var actualizando = false

        fun actualizarPrincipal() {
            actualizando = true
            val marcadas = hijas.count { it.isChecked }
            b.casillaTodos.checkedState = when (marcadas) {
                hijas.size -> MaterialCheckBox.STATE_CHECKED
                0 -> MaterialCheckBox.STATE_UNCHECKED
                else -> MaterialCheckBox.STATE_INDETERMINATE
            }
            val estado = when (b.casillaTodos.checkedState) {
                MaterialCheckBox.STATE_CHECKED -> "marcado"
                MaterialCheckBox.STATE_UNCHECKED -> "desmarcado"
                else -> "indeterminado"
            }
            b.resultadoCasillas.text = "Estado de la casilla principal: $estado"
            actualizando = false
        }

        hijas.forEach { it.setOnCheckedChangeListener { _, _ -> if (!actualizando) actualizarPrincipal() } }
        b.casillaTodos.addOnCheckedStateChangedListener { casilla, estado ->
            if (actualizando) return@addOnCheckedStateChangedListener
            // Al tocar la principal desde el estado indeterminado se marcan todas.
            val marcar = estado != MaterialCheckBox.STATE_UNCHECKED
            actualizando = true
            hijas.forEach { it.isChecked = marcar }
            actualizando = false
            actualizarPrincipal()
            casilla.jumpDrawablesToCurrentState()
        }
        actualizarPrincipal()
    }
}
