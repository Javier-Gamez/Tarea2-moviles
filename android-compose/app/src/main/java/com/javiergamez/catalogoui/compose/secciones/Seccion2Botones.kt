@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.javiergamez.catalogoui.compose.secciones

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.javiergamez.catalogoui.compose.componentes.ElementoCatalogo
import com.javiergamez.catalogoui.compose.componentes.PantallaSeccion
import com.javiergamez.catalogoui.compose.componentes.TextoResultado
import com.javiergamez.catalogoui.compose.componentes.rememberAvisos
import com.javiergamez.catalogoui.compose.navegacion.Seccion
import kotlinx.coroutines.delay

@Composable
fun Seccion2Botones() {
    val avisos = rememberAvisos()

    PantallaSeccion(Seccion.BOTONES) {
        item {
            ElementoCatalogo(
                nombre = "Botón relleno, con contorno y de texto",
                componente = "Button / OutlinedButton / TextButton",
                descripcion = "Los tres niveles de énfasis de un botón. El relleno se usa para la " +
                    "acción principal, el de contorno para acciones secundarias y el de texto para las menos importantes.",
            ) {
                var relleno by rememberSaveable { mutableIntStateOf(0) }
                var contorno by rememberSaveable { mutableIntStateOf(0) }
                var texto by rememberSaveable { mutableIntStateOf(0) }
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { relleno++ }) { Text("Relleno") }
                    OutlinedButton(onClick = { contorno++ }) { Text("Contorno") }
                    TextButton(onClick = { texto++ }) { Text("Texto") }
                }
                TextoResultado("Pulsaciones → relleno: $relleno · contorno: $contorno · texto: $texto")
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Botones con ícono",
                componente = "IconButton / FilledIconButton / Button + Icon",
                descripcion = "Un ícono comunica la acción de forma compacta. Los botones de solo " +
                    "ícono ahorran espacio y los que combinan ícono y texto son más claros.",
            ) {
                var favorito by rememberSaveable { mutableStateOf(false) }
                var resultado by rememberSaveable { mutableStateOf("Pulsa alguno de los botones.") }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    IconButton(onClick = {
                        favorito = !favorito
                        resultado = if (favorito) "Agregado a favoritos" else "Quitado de favoritos"
                    }) {
                        Icon(
                            if (favorito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (favorito) MaterialTheme.colorScheme.error else LocalContentColor.current,
                        )
                    }
                    FilledIconButton(onClick = {
                        resultado = "Compartir"
                        avisos.toast("Compartiendo contenido…")
                    }) {
                        Icon(Icons.Filled.Share, contentDescription = "Compartir")
                    }
                    Button(onClick = {
                        resultado = "Mensaje enviado"
                        avisos.snackbar("Mensaje enviado correctamente")
                    }) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                        Text("  Enviar")
                    }
                }
                TextoResultado(resultado)
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Botón de acción flotante",
                componente = "FloatingActionButton / ExtendedFloatingActionButton",
                descripcion = "Representa la acción más importante de una pantalla y flota sobre el " +
                    "contenido. La versión extendida agrega una etiqueta y puede contraerse al desplazarse.",
            ) {
                var creados by rememberSaveable { mutableIntStateOf(0) }
                var extendido by rememberSaveable { mutableStateOf(true) }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Mostrar etiqueta del FAB extendido", modifier = Modifier.weight(1f))
                    Switch(checked = extendido, onCheckedChange = { extendido = it })
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest, RoundedCornerShape(12.dp)),
                ) {
                    Text(
                        "Elementos creados: $creados",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    FloatingActionButton(
                        onClick = { creados++ },
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp),
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Crear")
                    }
                    ExtendedFloatingActionButton(
                        text = { Text("Redactar") },
                        icon = { Icon(Icons.Filled.Edit, contentDescription = null) },
                        expanded = extendido,
                        onClick = { avisos.snackbar("Abriendo el editor…") },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp),
                    )
                }
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Botón de alternancia y selector segmentado",
                componente = "IconToggleButton / SingleChoiceSegmentedButtonRow",
                descripcion = "Permiten activar o desactivar una opción o elegir una entre varias " +
                    "relacionadas. El estado seleccionado queda resaltado visualmente.",
            ) {
                val opciones = listOf("Día", "Semana", "Mes")
                var seleccion by rememberSaveable { mutableIntStateOf(0) }
                var guardado by rememberSaveable { mutableStateOf(false) }
                SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                    opciones.forEachIndexed { indice, etiqueta ->
                        SegmentedButton(
                            selected = seleccion == indice,
                            onClick = { seleccion = indice },
                            shape = SegmentedButtonDefaults.itemShape(index = indice, count = opciones.size),
                        ) { Text(etiqueta) }
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconToggleButton(checked = guardado, onCheckedChange = { guardado = it }) {
                        Icon(
                            if (guardado) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                            contentDescription = "Guardar",
                        )
                    }
                    Text(if (guardado) "Guardado en marcadores" else "Sin guardar")
                }
                TextoResultado("Vista seleccionada: ${opciones[seleccion]}")
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Botón deshabilitado y en estado de carga",
                componente = "Button(enabled) + CircularProgressIndicator",
                descripcion = "Un botón deshabilitado indica que la acción aún no está disponible. " +
                    "El estado de carga evita pulsaciones repetidas mientras se completa una operación.",
            ) {
                var habilitado by rememberSaveable { mutableStateOf(false) }
                var cargando by rememberSaveable { mutableStateOf(false) }
                var completadas by rememberSaveable { mutableIntStateOf(0) }
                LaunchedEffect(cargando) {
                    if (cargando) {
                        delay(2000)
                        cargando = false
                        completadas++
                        avisos.snackbar("Operación completada")
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Habilitar botón", modifier = Modifier.weight(1f))
                    Switch(checked = habilitado, onCheckedChange = { habilitado = it })
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { avisos.toast("¡El botón está habilitado!") }, enabled = habilitado) {
                        Text(if (habilitado) "Habilitado" else "Deshabilitado")
                    }
                    FilledTonalButton(onClick = { cargando = true }, enabled = !cargando) {
                        if (cargando) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                            Text("  Cargando…")
                        } else {
                            Icon(Icons.Filled.CloudDownload, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text("  Descargar")
                        }
                    }
                }
                TextoResultado("Descargas completadas: $completadas")
            }
        }
    }
}
