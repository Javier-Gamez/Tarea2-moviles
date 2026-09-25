@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.javiergamez.catalogoui.compose.secciones

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.javiergamez.catalogoui.compose.componentes.ElementoCatalogo
import com.javiergamez.catalogoui.compose.componentes.PantallaSeccion
import com.javiergamez.catalogoui.compose.componentes.TextoResultado
import com.javiergamez.catalogoui.compose.datos.CatalogoViewModel
import com.javiergamez.catalogoui.compose.datos.TamanoTexto
import com.javiergamez.catalogoui.compose.navegacion.Seccion
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt

private val LENGUAJES = listOf("Kotlin", "Dart", "Java", "Swift", "C#", "JavaScript")
private val INTERESES = listOf("Música", "Deportes", "Tecnología", "Viajes", "Cine", "Arte")

@Composable
fun Seccion3Seleccion(vm: CatalogoViewModel) {
    PantallaSeccion(Seccion.SELECCION) {
        item {
            ElementoCatalogo(
                nombre = "Casilla de verificación",
                componente = "Checkbox / TriStateCheckbox",
                descripcion = "Permite marcar varias opciones de forma independiente. La casilla " +
                    "principal muestra un estado indeterminado cuando solo algunas opciones están marcadas.",
            ) {
                val ingredientes = listOf("Queso", "Jamón", "Champiñones")
                var marcados by rememberSaveable { mutableStateOf(listOf(true, false, false)) }
                val estadoGeneral = when {
                    marcados.all { it } -> ToggleableState.On
                    marcados.none { it } -> ToggleableState.Off
                    else -> ToggleableState.Indeterminate
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.toggleable(
                        value = estadoGeneral == ToggleableState.On,
                        role = Role.Checkbox,
                        onValueChange = {
                            val nuevo = estadoGeneral != ToggleableState.On
                            marcados = marcados.map { nuevo }
                        },
                    ),
                ) {
                    TriStateCheckbox(state = estadoGeneral, onClick = null)
                    Text("Todos los ingredientes", style = MaterialTheme.typography.titleSmall)
                }
                Column(Modifier.padding(start = 24.dp)) {
                    ingredientes.forEachIndexed { i, nombre ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.toggleable(
                                value = marcados[i],
                                role = Role.Checkbox,
                                onValueChange = { v -> marcados = marcados.toMutableList().also { it[i] = v } },
                            ),
                        ) {
                            Checkbox(checked = marcados[i], onCheckedChange = null)
                            Text(nombre)
                        }
                    }
                }
                val estadoTexto = when (estadoGeneral) {
                    ToggleableState.On -> "marcado"
                    ToggleableState.Off -> "desmarcado"
                    ToggleableState.Indeterminate -> "indeterminado"
                }
                TextoResultado("Estado de la casilla principal: $estadoTexto")
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Botones de opción (conectado con la Sección 5)",
                componente = "RadioButton + Modifier.selectableGroup",
                descripcion = "Permiten elegir una sola opción de un grupo. La opción elegida aquí " +
                    "define el tamaño de los textos de ejemplo que se muestran en la Sección 5.",
            ) {
                Column(Modifier.selectableGroup()) {
                    TamanoTexto.entries.forEach { tamano ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = vm.tamanoTexto == tamano,
                                    onClick = { vm.tamanoTexto = tamano },
                                    role = Role.RadioButton,
                                ),
                        ) {
                            RadioButton(selected = vm.tamanoTexto == tamano, onClick = null)
                            Text(tamano.etiqueta, modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }
                Text(
                    "Vista previa del texto",
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize * vm.tamanoTexto.escala,
                )
                TextoResultado("Tamaño elegido: ${vm.tamanoTexto.etiqueta}. Revisa la Sección 5.")
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Interruptor",
                componente = "Switch",
                descripcion = "Activa o desactiva una opción de forma inmediata, sin necesidad de " +
                    "confirmar. Es ideal para ajustes que se aplican al momento.",
            ) {
                var avion by rememberSaveable { mutableStateOf(false) }
                var wifi by rememberSaveable { mutableStateOf(true) }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.AirplanemodeActive, contentDescription = null)
                    Text("Modo avión", modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp))
                    Switch(
                        checked = avion,
                        onCheckedChange = {
                            avion = it
                            if (it) wifi = false
                        },
                        thumbContent = {
                            Icon(
                                if (avion) Icons.Filled.Check else Icons.Filled.Close,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        },
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Wi-Fi", modifier = Modifier.weight(1f))
                    Switch(checked = wifi, onCheckedChange = { wifi = it }, enabled = !avion)
                }
                TextoResultado(
                    if (avion) "Modo avión activado: conexiones desactivadas"
                    else "Wi-Fi ${if (wifi) "encendido" else "apagado"}",
                )
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Deslizador de valor único y de rango",
                componente = "Slider / RangeSlider",
                descripcion = "Permiten elegir un valor numérico arrastrando un control sobre una " +
                    "pista. El de rango tiene dos controles para definir un mínimo y un máximo.",
            ) {
                var volumen by rememberSaveable { mutableFloatStateOf(40f) }
                var desde by rememberSaveable { mutableFloatStateOf(200f) }
                var hasta by rememberSaveable { mutableFloatStateOf(800f) }
                Text("Volumen: ${volumen.roundToInt()} %")
                Slider(value = volumen, onValueChange = { volumen = it }, valueRange = 0f..100f)
                Text("Precio: $${desde.roundToInt()} – $${hasta.roundToInt()} MXN")
                RangeSlider(
                    value = desde..hasta,
                    onValueChange = {
                        desde = it.start
                        hasta = it.endInclusive
                    },
                    valueRange = 0f..1000f,
                    steps = 19,
                )
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Lista desplegable",
                componente = "ExposedDropdownMenuBox (solo lectura)",
                descripcion = "Muestra la opción seleccionada y, al tocarla, despliega un menú con " +
                    "todas las alternativas. Ahorra espacio cuando hay muchas opciones.",
            ) {
                var expandido by rememberSaveable { mutableStateOf(false) }
                var lenguaje by rememberSaveable { mutableStateOf(LENGUAJES.first()) }
                ExposedDropdownMenuBox(expanded = expandido, onExpandedChange = { expandido = it }) {
                    OutlinedTextField(
                        value = lenguaje,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Lenguaje favorito") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                        modifier = Modifier
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth(),
                    )
                    ExposedDropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }) {
                        LENGUAJES.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    lenguaje = opcion
                                    expandido = false
                                },
                            )
                        }
                    }
                }
                TextoResultado("Elegiste: $lenguaje")
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Selector de fecha y de hora",
                componente = "DatePickerDialog / TimePicker",
                descripcion = "Diálogos que muestran un calendario o un reloj para elegir una fecha " +
                    "o una hora sin tener que escribirla, evitando errores de formato.",
            ) {
                var fecha by rememberSaveable { mutableStateOf<Long?>(null) }
                var hora by rememberSaveable { mutableStateOf<String?>(null) }
                var mostrarFecha by rememberSaveable { mutableStateOf(false) }
                var mostrarHora by rememberSaveable { mutableStateOf(false) }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { mostrarFecha = true }) {
                        Icon(Icons.Filled.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp))
                        Text("  Elegir fecha")
                    }
                    OutlinedButton(onClick = { mostrarHora = true }) {
                        Icon(Icons.Filled.Schedule, contentDescription = null, modifier = Modifier.size(18.dp))
                        Text("  Elegir hora")
                    }
                }
                val formato = remember { SimpleDateFormat("d 'de' MMMM 'de' yyyy", Locale.forLanguageTag("es-MX")).apply { timeZone = TimeZone.getTimeZone("UTC") } }
                TextoResultado("Fecha: ${fecha?.let { formato.format(Date(it)) } ?: "sin elegir"}")
                TextoResultado("Hora: ${hora ?: "sin elegir"}")

                if (mostrarFecha) {
                    val estado = rememberDatePickerState(initialSelectedDateMillis = fecha)
                    DatePickerDialog(
                        onDismissRequest = { mostrarFecha = false },
                        confirmButton = {
                            TextButton(onClick = {
                                fecha = estado.selectedDateMillis
                                mostrarFecha = false
                            }) { Text("Aceptar") }
                        },
                        dismissButton = {
                            TextButton(onClick = { mostrarFecha = false }) { Text("Cancelar") }
                        },
                    ) {
                        DatePicker(state = estado)
                    }
                }
                if (mostrarHora) {
                    val estado = rememberTimePickerState(is24Hour = true)
                    AlertDialog(
                        onDismissRequest = { mostrarHora = false },
                        title = { Text("Selecciona la hora") },
                        text = { TimePicker(state = estado) },
                        confirmButton = {
                            TextButton(onClick = {
                                hora = "%02d:%02d".format(estado.hour, estado.minute)
                                mostrarHora = false
                            }) { Text("Aceptar") }
                        },
                        dismissButton = {
                            TextButton(onClick = { mostrarHora = false }) { Text("Cancelar") }
                        },
                    )
                }
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Chips de filtro",
                componente = "FilterChip",
                descripcion = "Etiquetas compactas que se activan o desactivan para filtrar contenido. " +
                    "Se pueden seleccionar varias a la vez y muestran una marca cuando están activas.",
            ) {
                var seleccion by rememberSaveable { mutableStateOf(listOf("Tecnología")) }
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    INTERESES.forEach { interes ->
                        val activo = interes in seleccion
                        FilterChip(
                            selected = activo,
                            onClick = { seleccion = if (activo) seleccion - interes else seleccion + interes },
                            label = { Text(interes) },
                            leadingIcon = if (activo) {
                                { Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(FilterChipDefaults.IconSize)) }
                            } else {
                                null
                            },
                        )
                    }
                }
                TextoResultado(
                    if (seleccion.isEmpty()) "Sin filtros activos"
                    else "Filtros activos (${seleccion.size}): ${seleccion.joinToString()}",
                )
            }
        }
    }
}
