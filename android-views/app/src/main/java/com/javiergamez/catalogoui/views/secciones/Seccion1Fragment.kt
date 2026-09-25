package com.javiergamez.catalogoui.views.secciones

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.javiergamez.catalogoui.views.R
import com.javiergamez.catalogoui.views.componentes.dp
import com.javiergamez.catalogoui.views.componentes.mostrar
import com.javiergamez.catalogoui.views.componentes.snackbar
import com.javiergamez.catalogoui.views.databinding.FragmentSeccion1Binding
import com.javiergamez.catalogoui.views.datos.CatalogoViewModel
import com.javiergamez.catalogoui.views.navegacion.Seccion

private val ESTADOS = listOf(
    "Aguascalientes", "Baja California", "Baja California Sur", "Campeche", "Chiapas",
    "Chihuahua", "Ciudad de México", "Coahuila", "Colima", "Durango", "Estado de México",
    "Guanajuato", "Guerrero", "Hidalgo", "Jalisco", "Michoacán", "Morelos", "Nayarit",
    "Nuevo León", "Oaxaca", "Puebla", "Querétaro", "Quintana Roo", "San Luis Potosí",
    "Sinaloa", "Sonora", "Tabasco", "Tamaulipas", "Tlaxcala", "Veracruz", "Yucatán", "Zacatecas",
)

private val COMPONENTES = listOf(
    "Botón", "Casilla de verificación", "Campo de texto", "Chip", "Deslizador", "Diálogo",
    "Hoja inferior", "Interruptor", "Lista", "Pestañas", "Selector de fecha", "Snackbar", "Tarjeta",
)

class Seccion1Fragment : Fragment(R.layout.fragment_seccion1) {

    private val vm: CatalogoViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentSeccion1Binding.bind(view)
        b.encabezado.mostrar(Seccion.ENTRADA_TEXTO)

        // Campo simple: ambos campos actualizan el saludo.
        val saludar = { texto: String ->
            b.resultadoNombre.text = if (texto.isBlank()) "Aún no has escrito nada." else "¡Hola, ${texto.trim()}!"
        }
        b.campoNombre.doAfterTextChanged { saludar(it.toString()) }
        b.campoNombreRelleno.doAfterTextChanged { saludar(it.toString()) }

        // Validación de correo.
        b.campoCorreo.doAfterTextChanged { editable ->
            val correo = editable.toString().trim()
            val valido = Patterns.EMAIL_ADDRESS.matcher(correo).matches()
            b.capaCorreo.error = if (correo.isNotEmpty() && !valido) "Formato inválido. Ejemplo: nombre@dominio.com" else null
            b.capaCorreo.helperText = if (valido) "Correo válido" else "Campo obligatorio"
            b.capaCorreo.endIconMode = if (valido) TextInputLayout.END_ICON_CUSTOM else TextInputLayout.END_ICON_NONE
            if (valido) b.capaCorreo.setEndIconDrawable(R.drawable.ic_check_circle)
        }

        // Nivel de seguridad de la contraseña.
        b.campoClave.doAfterTextChanged { editable ->
            val clave = editable.toString()
            b.capaClave.helperText = when {
                clave.isEmpty() -> "Escribe al menos 8 caracteres"
                clave.length < 8 -> "Débil (${clave.length} caracteres)"
                clave.any { it.isDigit() } && clave.any { it.isUpperCase() } -> "Segura"
                else -> "Aceptable: agrega números y mayúsculas"
            }
        }

        // Sugerencias automáticas.
        b.campoEstado.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, ESTADOS))
        b.campoEstado.setOnItemClickListener { parent, _, posicion, _ ->
            b.resultadoEstado.text = "Seleccionaste: ${parent.getItemAtPosition(posicion)}"
        }
        b.campoEstado.doAfterTextChanged { editable ->
            val texto = editable.toString()
            if (texto !in ESTADOS) {
                val total = ESTADOS.count { it.contains(texto, ignoreCase = true) }
                b.resultadoEstado.text = "$total sugerencias disponibles"
            }
        }

        // Barra de búsqueda con resultados filtrados.
        fun mostrarResultados(consulta: String) {
            val resultados = COMPONENTES.filter { it.contains(consulta, ignoreCase = true) }
            b.resultadosBusqueda.removeAllViews()
            if (consulta.isNotEmpty()) {
                if (resultados.isEmpty()) agregarFila(b, "Sin resultados para “$consulta”", null)
                resultados.take(5).forEach { r ->
                    agregarFila(b, r) {
                        b.busqueda.setQuery(r, false)
                        b.busqueda.clearFocus()
                        b.resultadosBusqueda.removeAllViews()
                        b.resultadoBusqueda.text = "Abriste el resultado: $r"
                    }
                }
            }
            b.resultadoBusqueda.text = "${resultados.size} de ${COMPONENTES.size} componentes coinciden"
        }
        // Quita la línea inferior predeterminada; el fondo redondeado ya delimita la barra.
        b.busqueda.findViewById<View>(androidx.appcompat.R.id.search_plate)?.background = null
        b.busqueda.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(consulta: String): Boolean {
                b.busqueda.clearFocus()
                return true
            }

            override fun onQueryTextChange(consulta: String): Boolean {
                mostrarResultados(consulta)
                return true
            }
        })
        mostrarResultados("")

        // Conexión con la Sección 4.
        fun agregar() {
            val titulo = b.campoTitulo.text.toString()
            if (titulo.isBlank()) return
            vm.agregar(titulo, b.campoDescripcion.text.toString())
            snackbar("“${titulo.trim()}” se agregó a la Sección 4", "Ver") {
                findNavController().navigate(Seccion.LISTAS.destino)
            }
            b.campoTitulo.text = null
            b.campoDescripcion.text = null
            b.campoDescripcion.clearFocus()
        }
        b.campoTitulo.doAfterTextChanged { b.botonAgregar.isEnabled = !it.isNullOrBlank() }
        b.botonAgregar.setOnClickListener { agregar() }
        b.campoDescripcion.setOnEditorActionListener { _, accion, _ ->
            if (accion == EditorInfo.IME_ACTION_DONE) agregar()
            false
        }
        vm.elementos.observe(viewLifecycleOwner) { b.totalElementos.text = "${it.size} elementos" }
    }

    private fun agregarFila(b: FragmentSeccion1Binding, texto: String, alTocar: (() -> Unit)?) {
        val fila = TextView(requireContext()).apply {
            text = texto
            setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_BodyLarge)
            setPadding(dp(16), dp(12), dp(16), dp(12))
            setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0)
            compoundDrawablePadding = dp(16)
            if (alTocar != null) {
                val valor = android.util.TypedValue()
                context.theme.resolveAttribute(android.R.attr.selectableItemBackground, valor, true)
                setBackgroundResource(valor.resourceId)
                setOnClickListener { alTocar() }
            }
        }
        b.resultadosBusqueda.addView(fila)
    }
}
