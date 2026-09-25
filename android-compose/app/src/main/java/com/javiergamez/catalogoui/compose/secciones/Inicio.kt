package com.javiergamez.catalogoui.compose.secciones

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.javiergamez.catalogoui.compose.datos.CatalogoViewModel
import com.javiergamez.catalogoui.compose.navegacion.Seccion

/** Pantalla principal: presentación de la app y acceso a las seis secciones. */
@Composable
fun PantallaInicio(vm: CatalogoViewModel, onSeccion: (Seccion) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Catálogo de elementos de interfaz", style = MaterialTheme.typography.headlineSmall)
                    Text("Versión: Jetpack Compose", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    Text(
                        "Explora los componentes básicos de una interfaz móvil. Cada elemento incluye " +
                            "su nombre, una breve explicación y una demostración interactiva.",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
        item {
            Text(
                "Secciones",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
        items(Seccion.entries) { seccion ->
            ElevatedCard(onClick = { onSeccion(seccion) }, modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp),
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(44.dp)
                            .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape),
                    ) {
                        Icon(seccion.icono, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
                    }
                    Column(
                        Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                    ) {
                        Text("Sección ${seccion.numero}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                        Text(seccion.titulo, style = MaterialTheme.typography.titleMedium)
                        Text(seccion.resumen, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Abrir")
                }
            }
        }
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Link, contentDescription = null)
                        Text("  Conexiones entre secciones", style = MaterialTheme.typography.titleSmall)
                    }
                    Text(
                        "• Sección 1 → 4: el texto capturado se agrega a la lista vertical " +
                            "(actualmente hay ${vm.elementos.size} elementos).",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Text(
                        "• Sección 3 → 5: el tamaño de texto elegido (${vm.tamanoTexto.etiqueta}) " +
                            "cambia los ejemplos de tipografía.",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}
