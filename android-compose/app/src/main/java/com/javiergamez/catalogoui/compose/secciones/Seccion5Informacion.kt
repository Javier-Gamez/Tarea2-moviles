@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.javiergamez.catalogoui.compose.secciones

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.javiergamez.catalogoui.compose.R
import com.javiergamez.catalogoui.compose.componentes.ElementoCatalogo
import com.javiergamez.catalogoui.compose.componentes.PantallaSeccion
import com.javiergamez.catalogoui.compose.componentes.TextoResultado
import com.javiergamez.catalogoui.compose.componentes.rememberAvisos
import com.javiergamez.catalogoui.compose.datos.CatalogoViewModel
import com.javiergamez.catalogoui.compose.navegacion.Seccion
import kotlinx.coroutines.launch

const val URL_IMAGEN = "https://picsum.photos/id/1018/800/500"

private val MODOS_ESCALADO = listOf(
    "Recortar" to ContentScale.Crop,
    "Ajustar" to ContentScale.Fit,
    "Estirar" to ContentScale.FillBounds,
    "Original" to ContentScale.None,
)

@Composable
fun Seccion5Informacion(vm: CatalogoViewModel, onIrASeleccion: () -> Unit) {
    val avisos = rememberAvisos()

    PantallaSeccion(Seccion.INFORMACION) {
        item {
            ElementoCatalogo(
                nombre = "Textos con distintos estilos",
                componente = "Text + MaterialTheme.typography + AnnotatedString",
                descripcion = "La tipografía establece jerarquía: títulos grandes, cuerpo legible y " +
                    "etiquetas pequeñas. El tamaño de estos ejemplos depende de la opción elegida en la Sección 3.",
            ) {
                val escala = vm.tamanoTexto.escala
                var negrita by rememberSaveable { mutableStateOf(false) }
                var cursiva by rememberSaveable { mutableStateOf(false) }
                var subrayado by rememberSaveable { mutableStateOf(false) }

                @Composable
                fun Muestra(texto: String, estilo: TextStyle) {
                    Text(texto, style = estilo.copy(fontSize = estilo.fontSize * escala))
                }
                Muestra("Titular grande", MaterialTheme.typography.headlineMedium)
                Muestra("Título de sección", MaterialTheme.typography.titleLarge)
                Muestra("Texto de cuerpo para párrafos largos.", MaterialTheme.typography.bodyLarge)
                Muestra("ETIQUETA PEQUEÑA", MaterialTheme.typography.labelSmall)
                Text(
                    buildAnnotatedString {
                        append("Texto con ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("negrita") }
                        append(", ")
                        withStyle(SpanStyle(fontStyle = FontStyle.Italic)) { append("cursiva") }
                        append(", ")
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) { append("color") }
                        append(", ")
                        withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) { append("tachado") }
                        append(" y ")
                        withStyle(SpanStyle(fontFamily = FontFamily.Monospace)) { append("monoespaciado") }
                        append(".")
                    },
                    style = MaterialTheme.typography.bodyMedium.let { it.copy(fontSize = it.fontSize * escala) },
                )
                HorizontalDivider()
                Text("Aplica énfasis a la frase de ejemplo:", style = MaterialTheme.typography.labelLarge)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(selected = negrita, onClick = { negrita = !negrita }, label = { Text("Negrita") })
                    FilterChip(selected = cursiva, onClick = { cursiva = !cursiva }, label = { Text("Cursiva") })
                    FilterChip(selected = subrayado, onClick = { subrayado = !subrayado }, label = { Text("Subrayado") })
                }
                Text(
                    "El diseño es cómo funciona.",
                    style = MaterialTheme.typography.titleMedium.let { it.copy(fontSize = it.fontSize * escala) },
                    fontWeight = if (negrita) FontWeight.Bold else FontWeight.Normal,
                    fontStyle = if (cursiva) FontStyle.Italic else FontStyle.Normal,
                    textDecoration = if (subrayado) TextDecoration.Underline else TextDecoration.None,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextoResultado("Tamaño actual: ${vm.tamanoTexto.etiqueta}", Modifier.weight(1f))
                    TextButton(onClick = onIrASeleccion) { Text("Cambiar en Sección 3") }
                }
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Imagen local e imagen desde URL",
                componente = "Image(painterResource) / SubcomposeAsyncImage (Coil)",
                descripcion = "La primera imagen está incluida en la aplicación y la segunda se descarga " +
                    "de internet. El modo de escalado define cómo se adapta la imagen a su contenedor.",
            ) {
                var modo by rememberSaveable { mutableIntStateOf(0) }
                val escalado = MODOS_ESCALADO[modo].second
                SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                    MODOS_ESCALADO.forEachIndexed { i, (etiqueta, _) ->
                        SegmentedButton(
                            selected = modo == i,
                            onClick = { modo = i },
                            shape = SegmentedButtonDefaults.itemShape(i, MODOS_ESCALADO.size),
                            icon = {},
                        ) { Text(etiqueta, style = MaterialTheme.typography.labelMedium) }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Column(Modifier.weight(1f)) {
                        MarcoImagen {
                            Image(
                                painter = painterResource(R.drawable.paisaje),
                                contentDescription = "Paisaje de montañas incluido en la app",
                                contentScale = escalado,
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                        Text("Local (drawable)", style = MaterialTheme.typography.labelMedium)
                    }
                    Column(Modifier.weight(1f)) {
                        MarcoImagen {
                            SubcomposeAsyncImage(
                                model = URL_IMAGEN,
                                contentDescription = "Fotografía descargada de internet",
                                contentScale = escalado,
                                modifier = Modifier.fillMaxSize(),
                                loading = {
                                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        CircularProgressIndicator(Modifier.size(32.dp))
                                    }
                                },
                                error = {
                                    Column(
                                        Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                    ) {
                                        Icon(Icons.Filled.BrokenImage, contentDescription = null)
                                        Text("Sin conexión", style = MaterialTheme.typography.labelSmall)
                                    }
                                },
                            )
                        }
                        Text("Desde URL (Coil)", style = MaterialTheme.typography.labelMedium)
                    }
                }
                TextoResultado("Modo de escalado: ${MODOS_ESCALADO[modo].first}")
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Indicadores de progreso",
                componente = "LinearProgressIndicator / CircularProgressIndicator",
                descripcion = "Informan que una tarea está en curso. El modo determinado muestra el " +
                    "porcentaje avanzado; el indeterminado solo indica que se está trabajando.",
            ) {
                val scope = rememberCoroutineScope()
                val progreso = remember { Animatable(0f) }
                var indeterminado by rememberSaveable { mutableStateOf(true) }
                Text("Determinado: ${(progreso.value * 100).toInt()} %", style = MaterialTheme.typography.labelLarge)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    LinearProgressIndicator(progress = { progreso.value }, modifier = Modifier.weight(1f))
                    CircularProgressIndicator(progress = { progreso.value })
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            scope.launch {
                                progreso.snapTo(0f)
                                progreso.animateTo(1f, tween(3000, easing = LinearEasing))
                                avisos.snackbar("Descarga simulada completada")
                            }
                        },
                        enabled = !progreso.isRunning,
                    ) { Text("Iniciar") }
                    OutlinedButton(onClick = { scope.launch { progreso.snapTo(0f) } }) { Text("Reiniciar") }
                }
                HorizontalDivider()
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Mostrar indeterminados", style = MaterialTheme.typography.labelLarge, modifier = Modifier.weight(1f))
                    Switch(checked = indeterminado, onCheckedChange = { indeterminado = it })
                }
                if (indeterminado) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        LinearProgressIndicator(modifier = Modifier.weight(1f))
                        CircularProgressIndicator()
                    }
                }
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Toast y Snackbar",
                componente = "Toast (Android) / SnackbarHost",
                descripcion = "El toast es un aviso breve del sistema que desaparece solo. El snackbar " +
                    "aparece en la parte inferior de la app y puede incluir una acción, como deshacer.",
            ) {
                var archivado by rememberSaveable { mutableStateOf(false) }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { avisos.toast("Esto es un toast") }) { Text("Mostrar toast") }
                    Button(onClick = {
                        archivado = true
                        avisos.snackbar("Conversación archivada", "Deshacer") { archivado = false }
                    }) { Text("Mostrar snackbar") }
                }
                TextoResultado(if (archivado) "Estado: archivada (pulsa Deshacer en el snackbar)" else "Estado: en la bandeja")
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Diálogo de confirmación",
                componente = "AlertDialog",
                descripcion = "Interrumpe al usuario para confirmar una acción importante o irreversible. " +
                    "Presenta un título, una explicación y botones para aceptar o cancelar.",
            ) {
                var mostrar by rememberSaveable { mutableStateOf(false) }
                var resultado by rememberSaveable { mutableStateOf("Aún no se ha abierto el diálogo.") }
                FilledTonalButton(onClick = { mostrar = true }) {
                    Icon(Icons.Filled.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text("  Eliminar archivo")
                }
                TextoResultado(resultado)
                if (mostrar) {
                    AlertDialog(
                        onDismissRequest = {
                            mostrar = false
                            resultado = "Diálogo cerrado sin elegir."
                        },
                        icon = { Icon(Icons.Filled.Delete, contentDescription = null) },
                        title = { Text("¿Eliminar archivo?") },
                        text = { Text("El archivo “reporte.pdf” se eliminará de forma permanente. Esta acción no se puede deshacer.") },
                        confirmButton = {
                            TextButton(onClick = {
                                mostrar = false
                                resultado = "Confirmaste: archivo eliminado."
                            }) { Text("Eliminar") }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                mostrar = false
                                resultado = "Cancelaste la eliminación."
                            }) { Text("Cancelar") }
                        },
                    )
                }
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Hoja inferior",
                componente = "ModalBottomSheet",
                descripcion = "Panel que se desliza desde la parte inferior con opciones adicionales " +
                    "relacionadas con el contenido. Se cierra deslizándola hacia abajo o tocando fuera.",
            ) {
                val estado = rememberModalBottomSheetState()
                val scope = rememberCoroutineScope()
                var mostrar by rememberSaveable { mutableStateOf(false) }
                var resultado by rememberSaveable { mutableStateOf("Ninguna opción elegida.") }
                Button(onClick = { mostrar = true }) { Text("Abrir hoja inferior") }
                TextoResultado(resultado)
                if (mostrar) {
                    ModalBottomSheet(onDismissRequest = { mostrar = false }, sheetState = estado) {
                        Text(
                            "Compartir foto",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                        )
                        listOf<Pair<String, ImageVector>>(
                            "Compartir" to Icons.Filled.Share,
                            "Copiar enlace" to Icons.Filled.ContentCopy,
                            "Descargar" to Icons.Filled.Download,
                        ).forEach { (opcion, icono) ->
                            ListItem(
                                headlineContent = { Text(opcion) },
                                leadingContent = { Icon(icono, contentDescription = null) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        resultado = "Elegiste: $opcion"
                                        scope.launch { estado.hide() }.invokeOnCompletion { mostrar = false }
                                    },
                            )
                        }
                        Box(Modifier.height(24.dp))
                    }
                }
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Tarjeta, separador y distintivo",
                componente = "OutlinedCard / HorizontalDivider / BadgedBox",
                descripcion = "La tarjeta agrupa información relacionada, el separador divide contenido " +
                    "visualmente y el distintivo (badge) muestra un contador sobre un ícono.",
            ) {
                var notificaciones by rememberSaveable { mutableIntStateOf(3) }
                OutlinedCard(Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(16.dp)
                            .height(IntrinsicSize.Min),
                    ) {
                        BadgedBox(badge = {
                            if (notificaciones > 0) Badge { Text(if (notificaciones > 99) "99+" else "$notificaciones") }
                        }) {
                            Icon(Icons.Filled.Notifications, contentDescription = "Notificaciones", modifier = Modifier.size(32.dp))
                        }
                        VerticalDivider(Modifier.padding(horizontal = 16.dp).fillMaxHeight())
                        Column(Modifier.weight(1f)) {
                            Text("Notificaciones", style = MaterialTheme.typography.titleMedium)
                            Text(
                                if (notificaciones == 0) "Estás al día" else "Tienes $notificaciones sin leer",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                    HorizontalDivider()
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    ) {
                        TextButton(onClick = { notificaciones = 0 }) { Text("Marcar como leídas") }
                        TextButton(onClick = { notificaciones++ }) { Text("Recibir una") }
                    }
                }
            }
        }
    }
}

@Composable
private fun MarcoImagen(contenido: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
    ) { contenido() }
}
