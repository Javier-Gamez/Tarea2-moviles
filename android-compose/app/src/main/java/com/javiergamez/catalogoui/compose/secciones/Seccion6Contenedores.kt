@file:OptIn(ExperimentalMaterial3Api::class)

package com.javiergamez.catalogoui.compose.secciones

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.javiergamez.catalogoui.compose.R
import com.javiergamez.catalogoui.compose.componentes.ElementoCatalogo
import com.javiergamez.catalogoui.compose.componentes.PantallaSeccion
import com.javiergamez.catalogoui.compose.componentes.TextoResultado
import com.javiergamez.catalogoui.compose.navegacion.Seccion
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private val COLOR_A = Color(0xFF64B5F6)
private val COLOR_B = Color(0xFF81C784)
private val COLOR_C = Color(0xFFFFB74D)

@Composable
fun Seccion6Contenedores() {
    PantallaSeccion(Seccion.CONTENEDORES) {
        item {
            ElementoCatalogo(
                nombre = "Distribución en fila",
                componente = "Row + horizontalArrangement",
                descripcion = "Coloca los elementos uno junto a otro de forma horizontal. La " +
                    "distribución define cómo se reparte el espacio libre entre ellos.",
            ) {
                val opciones = listOf(
                    "Inicio" to Arrangement.Start,
                    "Centro" to Arrangement.Center,
                    "Entre" to Arrangement.SpaceBetween,
                    "Alrededor" to Arrangement.SpaceEvenly,
                )
                var elegido by rememberSaveable { mutableIntStateOf(0) }
                Selector(opciones.map { it.first }, elegido) { elegido = it }
                Row(
                    horizontalArrangement = opciones[elegido].second,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fondoDemo()
                        .padding(8.dp),
                ) {
                    Bloque("A", COLOR_A)
                    Bloque("B", COLOR_B)
                    Bloque("C", COLOR_C)
                }
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Distribución en columna",
                componente = "Column + horizontalAlignment",
                descripcion = "Apila los elementos de arriba hacia abajo. La alineación horizontal " +
                    "decide si se pegan a la izquierda, al centro o a la derecha.",
            ) {
                val opciones = listOf(
                    "Izquierda" to Alignment.Start,
                    "Centro" to Alignment.CenterHorizontally,
                    "Derecha" to Alignment.End,
                )
                var elegido by rememberSaveable { mutableIntStateOf(0) }
                Selector(opciones.map { it.first }, elegido) { elegido = it }
                Column(
                    horizontalAlignment = opciones[elegido].second,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fondoDemo()
                        .padding(8.dp),
                ) {
                    Bloque("A", COLOR_A, ancho = 120)
                    Bloque("B", COLOR_B, ancho = 80)
                    Bloque("C", COLOR_C, ancho = 160)
                }
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Distribución superpuesta",
                componente = "Box + Modifier.align",
                descripcion = "Coloca los elementos uno sobre otro, como capas. Es útil para poner " +
                    "texto o botones sobre una imagen. Elige dónde colocar la etiqueta.",
            ) {
                val opciones = listOf(
                    "Arriba" to Alignment.TopStart,
                    "Centro" to Alignment.Center,
                    "Abajo" to Alignment.BottomEnd,
                )
                var elegido by rememberSaveable { mutableIntStateOf(2) }
                Selector(opciones.map { it.first }, elegido) { elegido = it }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(12.dp)),
                ) {
                    Image(
                        painter = painterResource(R.drawable.paisaje),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(Brush.verticalGradient(listOf(Color.Transparent, Color(0x99000000)))),
                    )
                    Text(
                        "Capa de texto",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(opciones[elegido].second)
                            .padding(12.dp)
                            .background(Color(0x66000000), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                    )
                }
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Contenedor con desplazamiento vertical",
                componente = "Column + Modifier.verticalScroll",
                descripcion = "Permite ver contenido más alto que el espacio disponible desplazándolo " +
                    "con el dedo. Aquí también se puede saltar al final con un botón.",
            ) {
                val estado = rememberScrollState()
                val scope = rememberCoroutineScope()
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .fondoDemo()
                        .verticalScroll(estado)
                        .padding(12.dp),
                ) {
                    repeat(20) { i ->
                        Text("Párrafo ${i + 1}: contenido que requiere desplazamiento.")
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextoResultado("Desplazamiento: ${estado.value} px", Modifier.weight(1f))
                    OutlinedButton(onClick = {
                        scope.launch {
                            if (estado.value < estado.maxValue) estado.animateScrollTo(estado.maxValue)
                            else estado.animateScrollTo(0)
                        }
                    }) { Text(if (estado.value < estado.maxValue) "Ir al final" else "Ir al inicio") }
                }
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Barra superior con título y acciones",
                componente = "TopAppBar",
                descripcion = "Muestra el título de la pantalla y las acciones más frecuentes. Las " +
                    "acciones que no caben se agrupan en un menú de desbordamiento (tres puntos).",
            ) {
                var accion by rememberSaveable { mutableStateOf("Pulsa un ícono de la barra.") }
                var menu by rememberSaveable { mutableStateOf(false) }
                Surface(shape = RoundedCornerShape(12.dp), tonalElevation = 2.dp) {
                    TopAppBar(
                        title = { Text("Mis notas") },
                        navigationIcon = {
                            IconButton(onClick = { accion = "Menú de navegación" }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Menú")
                            }
                        },
                        actions = {
                            IconButton(onClick = { accion = "Buscar" }) {
                                Icon(Icons.Filled.Search, contentDescription = "Buscar")
                            }
                            IconButton(onClick = { accion = "Favoritos" }) {
                                Icon(Icons.Filled.Favorite, contentDescription = "Favoritos")
                            }
                            Box {
                                IconButton(onClick = { menu = true }) {
                                    Icon(Icons.Filled.MoreVert, contentDescription = "Más opciones")
                                }
                                DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) {
                                    listOf("Ajustes", "Ayuda", "Acerca de").forEach { opcion ->
                                        DropdownMenuItem(
                                            text = { Text(opcion) },
                                            onClick = {
                                                accion = "Menú: $opcion"
                                                menu = false
                                            },
                                        )
                                    }
                                }
                            }
                        },
                        windowInsets = WindowInsets(0),
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                        ),
                    )
                }
                TextoResultado("Acción: $accion")
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Barra de navegación inferior",
                componente = "NavigationBar + NavigationBarItem",
                descripcion = "Da acceso directo a entre tres y cinco destinos principales. Esta app " +
                    "usa además un menú lateral (ModalNavigationDrawer) para moverse entre secciones.",
            ) {
                val destinos = listOf(
                    Triple("Inicio", Icons.Filled.Home, "Contenido de la pantalla de inicio"),
                    Triple("Explorar", Icons.Filled.Explore, "Descubre contenido nuevo"),
                    Triple("Perfil", Icons.Filled.Person, "Tu información personal"),
                )
                var actual by rememberSaveable { mutableIntStateOf(0) }
                Surface(shape = RoundedCornerShape(12.dp), tonalElevation = 1.dp) {
                    Column {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(destinos[actual].second, contentDescription = null, modifier = Modifier.size(32.dp))
                                Text(destinos[actual].third)
                            }
                        }
                        NavigationBar(windowInsets = WindowInsets(0)) {
                            destinos.forEachIndexed { i, (etiqueta, icono, _) ->
                                NavigationBarItem(
                                    selected = actual == i,
                                    onClick = { actual = i },
                                    icon = { Icon(icono, contentDescription = null) },
                                    label = { Text(etiqueta) },
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Distribución con pesos proporcionales",
                componente = "Row + Modifier.weight",
                descripcion = "Reparte el ancho disponible en proporción al peso de cada elemento. " +
                    "Mueve el deslizador para cambiar el peso del bloque A frente al B.",
            ) {
                var pesoA by rememberSaveable { mutableFloatStateOf(1f) }
                val pesoB = 1f
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fondoDemo()
                        .padding(8.dp),
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(pesoA)
                            .height(56.dp)
                            .background(COLOR_A, RoundedCornerShape(8.dp)),
                    ) { Text("A · ${pesoA.roundToInt()}", color = Color.Black) }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(pesoB)
                            .height(56.dp)
                            .background(COLOR_B, RoundedCornerShape(8.dp)),
                    ) { Text("B · 1", color = Color.Black) }
                }
                Slider(value = pesoA, onValueChange = { pesoA = it }, valueRange = 1f..5f, steps = 3)
                val porcentaje = (pesoA.roundToInt() * 100f / (pesoA.roundToInt() + pesoB)).roundToInt()
                TextoResultado("A ocupa $porcentaje % del ancho y B el ${100 - porcentaje} %")
            }
        }
    }
}

@Composable
private fun Selector(opciones: List<String>, elegido: Int, onElegir: (Int) -> Unit) {
    SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
        opciones.forEachIndexed { i, etiqueta ->
            SegmentedButton(
                selected = elegido == i,
                onClick = { onElegir(i) },
                shape = SegmentedButtonDefaults.itemShape(i, opciones.size),
                icon = {},
            ) { Text(etiqueta, style = MaterialTheme.typography.labelMedium) }
        }
    }
}

@Composable
private fun Bloque(texto: String, color: Color, ancho: Int = 56) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(width = ancho.dp, height = 48.dp)
            .background(color, RoundedCornerShape(8.dp)),
    ) { Text(texto, color = Color.Black, fontWeight = FontWeight.Bold) }
}

@Composable
private fun Modifier.fondoDemo(): Modifier =
    this.background(MaterialTheme.colorScheme.surfaceContainerHighest, RoundedCornerShape(12.dp))
