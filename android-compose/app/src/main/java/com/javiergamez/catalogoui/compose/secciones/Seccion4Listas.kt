@file:OptIn(ExperimentalMaterial3Api::class)

package com.javiergamez.catalogoui.compose.secciones

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.javiergamez.catalogoui.compose.componentes.ElementoCatalogo
import com.javiergamez.catalogoui.compose.componentes.PantallaSeccion
import com.javiergamez.catalogoui.compose.componentes.TextoResultado
import com.javiergamez.catalogoui.compose.componentes.rememberAvisos
import com.javiergamez.catalogoui.compose.datos.CatalogoViewModel
import com.javiergamez.catalogoui.compose.datos.ElementoLista
import com.javiergamez.catalogoui.compose.navegacion.Seccion
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val COLORES_CUADRICULA = listOf(
    Color(0xFFE57373), Color(0xFFF06292), Color(0xFFBA68C8), Color(0xFF9575CD),
    Color(0xFF7986CB), Color(0xFF64B5F6), Color(0xFF4DD0E1), Color(0xFF4DB6AC),
    Color(0xFF81C784), Color(0xFFDCE775), Color(0xFFFFD54F), Color(0xFFFF8A65),
)

private val CORREOS = listOf(
    "Recordatorio de clase", "Entrega de la práctica", "Boletín semanal",
    "Invitación a conferencia", "Confirmación de registro", "Promoción de temporada",
)

/** Elementos de la lista con encabezados: dos tipos distintos. */
private sealed interface FilaAgrupada {
    data class Contacto(val nombre: String, val detalle: String) : FilaAgrupada
    data class Anuncio(val texto: String) : FilaAgrupada
}

private val GRUPOS = listOf(
    "Favoritos" to listOf(
        FilaAgrupada.Contacto("Ana Martínez", "Diseñadora"),
        FilaAgrupada.Contacto("Bruno Díaz", "Desarrollador"),
        FilaAgrupada.Anuncio("Tus favoritos se sincronizan en todos tus dispositivos."),
    ),
    "Compañeros" to listOf(
        FilaAgrupada.Contacto("Carla Núñez", "Grupo 7CM1"),
        FilaAgrupada.Contacto("Diego Ramos", "Grupo 7CM1"),
        FilaAgrupada.Contacto("Elena Soto", "Grupo 7CM2"),
        FilaAgrupada.Anuncio("Invita a tus compañeros a probar la aplicación."),
    ),
    "Profesores" to listOf(
        FilaAgrupada.Contacto("Fernando Gil", "Aplicaciones Móviles"),
        FilaAgrupada.Contacto("Gabriela Ortiz", "Ingeniería de Software"),
    ),
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Seccion4Listas(vm: CatalogoViewModel, onDetalle: (Int) -> Unit) {
    val avisos = rememberAvisos()

    PantallaSeccion(Seccion.LISTAS) {
        item {
            ElementoCatalogo(
                nombre = "Lista vertical y detalle de elemento",
                componente = "LazyColumn + items(key) + navegación",
                descripcion = "Muestra muchos elementos de forma eficiente: solo se dibujan los visibles. " +
                    "Toca un elemento para abrir su detalle. Los que agregues en la Sección 1 aparecen aquí.",
            ) {
                Text("${vm.elementos.size} elementos", style = MaterialTheme.typography.labelLarge)
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLowest,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(360.dp),
                ) {
                    LazyColumn {
                        items(vm.elementos, key = { it.id }) { elemento ->
                            FilaElemento(elemento, onClick = { onDetalle(elemento.id) })
                            HorizontalDivider()
                        }
                    }
                }
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Cuadrícula de elementos",
                componente = "LazyVerticalGrid(GridCells.Fixed(3))",
                descripcion = "Organiza los elementos en filas y columnas, útil para galerías de " +
                    "fotos o catálogos. Toca las celdas para seleccionarlas.",
            ) {
                var seleccionadas by rememberSaveable { mutableStateOf(setOf<Int>()) }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    userScrollEnabled = false,
                    modifier = Modifier.height(288.dp),
                ) {
                    itemsIndexed(COLORES_CUADRICULA) { indice, color ->
                        val activa = indice in seleccionadas
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .aspectRatio(1.5f)
                                .background(color, RoundedCornerShape(12.dp))
                                .then(
                                    if (activa) Modifier.border(3.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(12.dp))
                                    else Modifier,
                                )
                                .clickable {
                                    seleccionadas = if (activa) seleccionadas - indice else seleccionadas + indice
                                },
                        ) {
                            if (activa) {
                                Icon(Icons.Filled.CheckCircle, contentDescription = "Seleccionada", tint = Color.White)
                            } else {
                                Text("${indice + 1}", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                TextoResultado("Celdas seleccionadas: ${seleccionadas.size} de ${COLORES_CUADRICULA.size}")
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Lista con encabezados de sección",
                componente = "LazyColumn + stickyHeader",
                descripcion = "Agrupa los elementos bajo encabezados que permanecen fijos al desplazarse. " +
                    "Combina dos tipos de fila: contactos y avisos con un diseño distinto.",
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLowest,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp),
                ) {
                    LazyColumn {
                        GRUPOS.forEach { (grupo, filas) ->
                            stickyHeader(key = grupo) {
                                Text(
                                    grupo,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.primaryContainer)
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                )
                            }
                            items(filas) { fila ->
                                when (fila) {
                                    is FilaAgrupada.Contacto -> ListItem(
                                        headlineContent = { Text(fila.nombre) },
                                        supportingContent = { Text(fila.detalle) },
                                        leadingContent = { Avatar(fila.nombre) },
                                        modifier = Modifier.clickable { avisos.toast("Contacto: ${fila.nombre}") },
                                    )
                                    is FilaAgrupada.Anuncio -> Card(
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 12.dp, vertical = 6.dp),
                                    ) {
                                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Filled.Campaign, contentDescription = null)
                                            Text(fila.texto, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 12.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Deslizar para eliminar",
                componente = "SwipeToDismissBox",
                descripcion = "Desliza un correo hacia cualquier lado para eliminarlo. Se muestra un " +
                    "fondo rojo con un ícono de papelera y un snackbar permite deshacer la acción.",
            ) {
                val correos = remember { mutableStateListOf(*CORREOS.toTypedArray()) }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLowest,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column {
                        if (correos.isEmpty()) {
                            Text(
                                "Bandeja vacía",
                                modifier = Modifier.padding(16.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        correos.forEach { correo ->
                            androidx.compose.runtime.key(correo) {
                                FilaDeslizable(correo) {
                                    val indice = correos.indexOf(correo)
                                    correos.remove(correo)
                                    avisos.snackbar("Correo eliminado", "Deshacer") {
                                        if (correo !in correos) correos.add(indice.coerceAtMost(correos.size), correo)
                                    }
                                }
                            }
                        }
                    }
                }
                OutlinedButton(onClick = {
                    correos.clear()
                    correos.addAll(CORREOS)
                }) { Text("Restaurar correos") }
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Actualizar arrastrando hacia abajo",
                componente = "PullToRefreshBox",
                descripcion = "Al arrastrar la lista hacia abajo desde el inicio aparece un indicador " +
                    "de carga y se obtienen elementos nuevos. Es el gesto estándar para refrescar.",
            ) {
                val scope = rememberCoroutineScope()
                var actualizando by remember { mutableStateOf(false) }
                var contador by rememberSaveable { mutableIntStateOf(3) }
                val noticias = remember {
                    mutableStateListOf("Noticia 3 · inicial", "Noticia 2 · inicial", "Noticia 1 · inicial")
                }
                val formato = remember { SimpleDateFormat("HH:mm:ss", Locale.forLanguageTag("es-MX")) }
                PullToRefreshBox(
                    isRefreshing = actualizando,
                    onRefresh = {
                        scope.launch {
                            actualizando = true
                            delay(1500)
                            contador++
                            noticias.add(0, "Noticia $contador · ${formato.format(Date())}")
                            actualizando = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .background(MaterialTheme.colorScheme.surfaceContainerLowest, RoundedCornerShape(12.dp)),
                ) {
                    LazyColumn(Modifier.fillMaxSize()) {
                        items(noticias, key = { it }) { noticia ->
                            ListItem(
                                headlineContent = { Text(noticia) },
                                leadingContent = { Icon(Icons.Filled.Newspaper, contentDescription = null) },
                            )
                        }
                    }
                }
                TextoResultado("Arrastra la lista hacia abajo. Total: ${noticias.size} noticias")
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Estado vacío",
                componente = "Column + Icon + Text (composición propia)",
                descripcion = "Cuando no hay elementos se muestra una ilustración con un mensaje que " +
                    "explica la situación y una acción para salir de ella, en lugar de una pantalla en blanco.",
            ) {
                val tareas = remember { mutableStateListOf("Repasar apuntes", "Subir la tarea") }
                if (tareas.isEmpty()) {
                    EstadoVacio(onAccion = { tareas.addAll(listOf("Repasar apuntes", "Subir la tarea", "Leer capítulo 3")) })
                } else {
                    tareas.forEach { tarea ->
                        ListItem(
                            headlineContent = { Text(tarea) },
                            leadingContent = { Icon(Icons.Filled.CheckCircle, contentDescription = null) },
                        )
                    }
                    Button(onClick = { tareas.clear() }) { Text("Vaciar lista") }
                }
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Pestañas con contenido deslizable",
                componente = "PrimaryTabRow + HorizontalPager",
                descripcion = "Dividen el contenido en categorías del mismo nivel. Se puede cambiar de " +
                    "pestaña tocándola o deslizando el contenido hacia los lados.",
            ) {
                val pestanas = listOf(
                    Triple("Recientes", Icons.Filled.History, "Archivos abiertos hoy"),
                    Triple("Favoritos", Icons.Filled.Star, "Archivos marcados con estrella"),
                    Triple("Archivados", Icons.Filled.Archive, "Archivos guardados para después"),
                )
                val pager = rememberPagerState { pestanas.size }
                val scope = rememberCoroutineScope()
                PrimaryTabRow(selectedTabIndex = pager.currentPage) {
                    pestanas.forEachIndexed { i, (titulo, icono, _) ->
                        Tab(
                            selected = pager.currentPage == i,
                            onClick = { scope.launch { pager.animateScrollToPage(i) } },
                            text = { Text(titulo) },
                            icon = { Icon(icono, contentDescription = null) },
                        )
                    }
                }
                HorizontalPager(
                    state = pager,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                ) { pagina ->
                    val (titulo, icono, texto) = pestanas[pagina]
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                            .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(12.dp)),
                    ) {
                        Icon(icono, contentDescription = null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.onSecondaryContainer)
                        Text(titulo, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSecondaryContainer)
                        Text(texto, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    }
                }
                TextoResultado("Pestaña actual: ${pestanas[pager.currentPage].first}")
            }
        }
    }
}

@Composable
private fun FilaElemento(elemento: ElementoLista, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(elemento.titulo) },
        supportingContent = { Text(elemento.descripcion, maxLines = 1) },
        leadingContent = { Avatar(elemento.titulo) },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (elemento.deUsuario) {
                    Text(
                        "Nuevo",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.tertiaryContainer, RoundedCornerShape(8.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                    )
                }
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
            }
        },
        modifier = Modifier.clickable(onClick = onClick),
    )
}

@Composable
fun Avatar(texto: String, tamano: Int = 40) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(tamano.dp)
            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
    ) {
        Text(
            texto.take(1).uppercase(),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = if (tamano > 48) MaterialTheme.typography.headlineMedium else MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
private fun FilaDeslizable(texto: String, onEliminar: () -> Unit) {
    val estado = rememberSwipeToDismissBoxState()
    LaunchedEffect(estado.currentValue) {
        if (estado.currentValue != SwipeToDismissBoxValue.Settled) onEliminar()
    }
    SwipeToDismissBox(
        state = estado,
        backgroundContent = {
            val alInicio = estado.dismissDirection == SwipeToDismissBoxValue.StartToEnd
            Box(
                contentAlignment = if (alInicio) Alignment.CenterStart else Alignment.CenterEnd,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(horizontal = 20.dp),
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.onErrorContainer)
            }
        },
    ) {
        ListItem(
            headlineContent = { Text(texto) },
            supportingContent = { Text("Desliza para eliminar") },
            leadingContent = { Icon(Icons.Filled.Email, contentDescription = null) },
        )
    }
}

@Composable
private fun EstadoVacio(onAccion: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(96.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape),
        ) {
            Icon(
                Icons.Filled.Inbox,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
        Text("No hay tareas pendientes", style = MaterialTheme.typography.titleMedium)
        Text(
            "Cuando agregues tareas aparecerán aquí.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Button(onClick = onAccion) { Text("Agregar tareas de ejemplo") }
    }
}

/** Pantalla de detalle de un elemento de la lista vertical. */
@Composable
fun PantallaDetalle(vm: CatalogoViewModel, id: Int, onRegresar: () -> Unit) {
    val avisos = rememberAvisos()
    val elemento = vm.buscar(id)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        if (elemento == null) {
            Text("El elemento ya no existe.")
            Button(onClick = onRegresar) { Text("Regresar") }
            return@Column
        }
        Avatar(elemento.titulo, tamano = 96)
        Text(elemento.titulo, style = MaterialTheme.typography.headlineMedium)
        Text(
            elemento.descripcion,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Identificador: ${elemento.id}")
                Text("Origen: ${if (elemento.deUsuario) "capturado en la Sección 1" else "lista predeterminada"}")
                Text("Posición en la lista: ${vm.elementos.indexOf(elemento) + 1} de ${vm.elementos.size}")
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onRegresar) { Text("Regresar") }
            Button(onClick = {
                val titulo = elemento.titulo
                vm.eliminar(elemento.id)
                avisos.snackbar("Se eliminó “$titulo”")
                onRegresar()
            }) {
                Icon(Icons.Filled.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                Text("  Eliminar")
            }
        }
    }
}
