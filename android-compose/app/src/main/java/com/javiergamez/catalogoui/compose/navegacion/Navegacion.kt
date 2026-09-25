package com.javiergamez.catalogoui.compose.navegacion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.SmartButton
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.javiergamez.catalogoui.compose.componentes.LocalSnackbar
import com.javiergamez.catalogoui.compose.datos.CatalogoViewModel
import com.javiergamez.catalogoui.compose.secciones.PantallaInicio
import com.javiergamez.catalogoui.compose.secciones.Seccion1EntradaTexto
import com.javiergamez.catalogoui.compose.secciones.Seccion2Botones
import kotlinx.coroutines.launch

const val RUTA_INICIO = "inicio"
const val RUTA_DETALLE = "detalle"

/** Las seis secciones del catálogo; cada una es un destino composable. */
enum class Seccion(
    val ruta: String,
    val numero: Int,
    val titulo: String,
    val resumen: String,
    val icono: ImageVector,
) {
    ENTRADA_TEXTO("entrada_texto", 1, "Entrada de texto", "Campos para capturar información escrita.", Icons.Filled.TextFields),
    BOTONES("botones", 2, "Botones y acciones", "Controles que ejecutan acciones al pulsarse.", Icons.Filled.SmartButton),
    SELECCION("seleccion", 3, "Elementos de selección", "Controles para elegir opciones y valores.", Icons.Filled.CheckBox),
    LISTAS("listas", 4, "Listas y colecciones", "Formas de mostrar conjuntos de elementos.", Icons.AutoMirrored.Filled.List),
    INFORMACION("informacion", 5, "Información y retroalimentación", "Elementos que comunican estados y resultados.", Icons.Filled.Info),
    CONTENEDORES("contenedores", 6, "Contenedores y estructura", "Distribuciones que organizan la pantalla.", Icons.Filled.Dashboard),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppCatalogo(vm: CatalogoViewModel = viewModel()) {
    val nav = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }
    val entrada by nav.currentBackStackEntryAsState()
    val ruta = entrada?.destination?.route
    val seccionActual = Seccion.entries.find { it.ruta == ruta }
    val enDetalle = ruta?.startsWith(RUTA_DETALLE) == true
    val enInicio = ruta == null || ruta == RUTA_INICIO

    fun irA(destino: String) {
        scope.launch { drawerState.close() }
        nav.navigate(destino) {
            popUpTo(RUTA_INICIO)
            launchSingleTop = true
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        // Solo se permite el gesto para cerrar, así no interfiere con deslizadores y pestañas.
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                Text(
                    "Catálogo de UI",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 28.dp, vertical = 16.dp),
                )
                NavigationDrawerItem(
                    label = { Text("Inicio") },
                    icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                    selected = enInicio,
                    onClick = { irA(RUTA_INICIO) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                )
                HorizontalDivider(Modifier.padding(horizontal = 28.dp, vertical = 8.dp))
                Seccion.entries.forEach { seccion ->
                    NavigationDrawerItem(
                        label = { Text("${seccion.numero}. ${seccion.titulo}") },
                        icon = { Icon(seccion.icono, contentDescription = null) },
                        selected = seccion == seccionActual,
                        onClick = { irA(seccion.ruta) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    )
                }
            }
        },
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            when {
                                enDetalle -> "Detalle del elemento"
                                seccionActual != null -> seccionActual.titulo
                                else -> "Catálogo de UI"
                            },
                        )
                    },
                    navigationIcon = {
                        if (enDetalle) {
                            IconButton(onClick = { nav.popBackStack() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                            }
                        } else {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Abrir menú")
                            }
                        }
                    },
                    actions = {
                        if (!enInicio) {
                            IconButton(onClick = { irA(RUTA_INICIO) }) {
                                Icon(Icons.Filled.Home, contentDescription = "Ir a la pantalla principal")
                            }
                        }
                    },
                )
            },
            snackbarHost = { SnackbarHost(snackbar) },
        ) { padding ->
            CompositionLocalProvider(LocalSnackbar provides snackbar) {
                NavHost(
                    navController = nav,
                    startDestination = RUTA_INICIO,
                    modifier = Modifier.padding(padding),
                ) {
                    composable(RUTA_INICIO) {
                        PantallaInicio(vm = vm, onSeccion = { irA(it.ruta) })
                    }
                    destinosSecciones(nav, vm, ::irA)
                }
            }
        }
    }
}

private fun NavGraphBuilder.destinosSecciones(
    nav: NavHostController,
    vm: CatalogoViewModel,
    irA: (String) -> Unit,
) {
    Seccion.entries.forEach { seccion ->
        composable(seccion.ruta) {
            when (seccion) {
                Seccion.ENTRADA_TEXTO -> Seccion1EntradaTexto(vm, onIrALista = { irA(Seccion.LISTAS.ruta) })
                Seccion.BOTONES -> Seccion2Botones()
                else -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Sección en construcción")
                }
            }
        }
    }
    composable(
        route = "$RUTA_DETALLE/{id}",
        arguments = listOf(navArgument("id") { type = NavType.IntType }),
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Detalle en construcción")
        }
    }
}
