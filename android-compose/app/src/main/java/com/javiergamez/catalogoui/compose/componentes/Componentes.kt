package com.javiergamez.catalogoui.compose.componentes

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.javiergamez.catalogoui.compose.navegacion.Seccion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/** SnackbarHost del Scaffold principal, disponible para todas las secciones. */
val LocalSnackbar = staticCompositionLocalOf<SnackbarHostState> {
    error("No se proporcionó un SnackbarHostState")
}

/** Ayudante para mostrar mensajes breves (toast) y mensajes con acción (snackbar). */
class Avisos(
    private val snackbar: SnackbarHostState,
    private val scope: CoroutineScope,
    private val context: Context,
) {
    fun snackbar(mensaje: String, accion: String? = null, alAccion: () -> Unit = {}) {
        scope.launch {
            snackbar.currentSnackbarData?.dismiss()
            val resultado = snackbar.showSnackbar(
                message = mensaje,
                actionLabel = accion,
                duration = SnackbarDuration.Short,
            )
            if (resultado == SnackbarResult.ActionPerformed) alAccion()
        }
    }

    fun toast(mensaje: String) {
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun rememberAvisos(): Avisos {
    val snackbar = LocalSnackbar.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    return remember(snackbar, scope, context) { Avisos(snackbar, scope, context) }
}

/** Pantalla base de cada sección: encabezado + tarjetas de elementos. */
@Composable
fun PantallaSeccion(seccion: Seccion, contenido: LazyListScope.() -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item { EncabezadoSeccion(seccion) }
        contenido()
    }
}

@Composable
fun EncabezadoSeccion(seccion: Seccion) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 4.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
        ) {
            Icon(
                seccion.icono,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
        Column(Modifier.padding(start = 16.dp)) {
            Text(
                "Sección ${seccion.numero}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(seccion.titulo, style = MaterialTheme.typography.headlineSmall)
            Text(
                seccion.resumen,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/**
 * Tarjeta que documenta un elemento del catálogo: nombre, componente usado,
 * explicación breve y una demostración interactiva.
 */
@Composable
fun ElementoCatalogo(
    nombre: String,
    descripcion: String,
    componente: String,
    demo: @Composable ColumnScope.() -> Unit,
) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    componente,
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.secondaryContainer,
                            RoundedCornerShape(6.dp),
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                )
                Text(
                    descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            HorizontalDivider()
            demo()
        }
    }
}

/** Texto que muestra el resultado de interactuar con una demostración. */
@Composable
fun TextoResultado(texto: String, modifier: Modifier = Modifier) {
    Text(
        texto,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier,
    )
}
