@file:OptIn(ExperimentalMaterial3Api::class)

package com.javiergamez.catalogoui.compose.secciones

import android.util.Patterns
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.javiergamez.catalogoui.compose.componentes.ElementoCatalogo
import com.javiergamez.catalogoui.compose.componentes.PantallaSeccion
import com.javiergamez.catalogoui.compose.componentes.TextoResultado
import com.javiergamez.catalogoui.compose.componentes.rememberAvisos
import com.javiergamez.catalogoui.compose.datos.CatalogoViewModel
import com.javiergamez.catalogoui.compose.navegacion.Seccion

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

@Composable
fun Seccion1EntradaTexto(vm: CatalogoViewModel, onIrALista: () -> Unit) {
    val avisos = rememberAvisos()

    PantallaSeccion(Seccion.ENTRADA_TEXTO) {
        item {
            ElementoCatalogo(
                nombre = "Campo de texto simple",
                componente = "TextField / OutlinedTextField",
                descripcion = "Permite escribir una línea de texto. La etiqueta indica qué dato se " +
                    "espera y el texto de ayuda (placeholder) sugiere un ejemplo mientras está vacío.",
            ) {
                var nombre by rememberSaveable { mutableStateOf("") }
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    placeholder = { Text("Ej. Ana López") },
                    leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Variante rellena") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                TextoResultado(if (nombre.isBlank()) "Aún no has escrito nada." else "¡Hola, ${nombre.trim()}!")
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Campo con validación",
                componente = "OutlinedTextField(isError, supportingText)",
                descripcion = "Revisa el contenido mientras se escribe y muestra un mensaje de error " +
                    "debajo del campo cuando el formato no es correcto, antes de enviar el formulario.",
            ) {
                var correo by rememberSaveable { mutableStateOf("") }
                val valido = Patterns.EMAIL_ADDRESS.matcher(correo).matches()
                val conError = correo.isNotEmpty() && !valido
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it.trim() },
                    label = { Text("Correo electrónico *") },
                    isError = conError,
                    supportingText = {
                        Text(
                            when {
                                conError -> "Formato inválido. Ejemplo: nombre@dominio.com"
                                valido -> "Correo válido"
                                else -> "Campo obligatorio"
                            },
                        )
                    },
                    trailingIcon = {
                        when {
                            conError -> Icon(Icons.Filled.Error, contentDescription = "Error")
                            valido -> Icon(
                                Icons.Filled.CheckCircle,
                                contentDescription = "Válido",
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Campo de contraseña",
                componente = "OutlinedTextField + PasswordVisualTransformation",
                descripcion = "Oculta los caracteres escritos para proteger información sensible. " +
                    "El ícono del ojo permite mostrar u ocultar el contenido para verificarlo.",
            ) {
                var clave by rememberSaveable { mutableStateOf("") }
                var visible by rememberSaveable { mutableStateOf(false) }
                OutlinedTextField(
                    value = clave,
                    onValueChange = { clave = it },
                    label = { Text("Contraseña") },
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { visible = !visible }) {
                            Icon(
                                if (visible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = if (visible) "Ocultar contraseña" else "Mostrar contraseña",
                            )
                        }
                    },
                    visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    supportingText = {
                        val nivel = when {
                            clave.isEmpty() -> "Escribe al menos 8 caracteres"
                            clave.length < 8 -> "Débil (${clave.length} caracteres)"
                            clave.any { it.isDigit() } && clave.any { it.isUpperCase() } -> "Segura"
                            else -> "Aceptable: agrega números y mayúsculas"
                        }
                        Text(nivel)
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Tipos de teclado",
                componente = "KeyboardOptions(keyboardType)",
                descripcion = "Cada campo solicita al sistema el teclado más adecuado para el dato: " +
                    "solo números, con arroba para correos o con el marcador telefónico.",
            ) {
                var edad by rememberSaveable { mutableStateOf("") }
                var correo by rememberSaveable { mutableStateOf("") }
                var telefono by rememberSaveable { mutableStateOf("") }
                OutlinedTextField(
                    value = edad,
                    onValueChange = { nuevo -> edad = nuevo.filter { it.isDigit() }.take(3) },
                    label = { Text("Edad (numérico)") },
                    leadingIcon = { Icon(Icons.Filled.Cake, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo (teclado de correo)") },
                    leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = telefono,
                    onValueChange = { nuevo -> telefono = nuevo.filter { it.isDigit() || it in "+ -" }.take(15) },
                    label = { Text("Teléfono (marcador)") },
                    leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Campo multilínea",
                componente = "OutlinedTextField(minLines, maxLines)",
                descripcion = "Admite varias líneas para textos largos como comentarios o notas. " +
                    "Crece conforme se escribe y un contador indica cuántos caracteres quedan.",
            ) {
                val limite = 200
                var nota by rememberSaveable { mutableStateOf("") }
                OutlinedTextField(
                    value = nota,
                    onValueChange = { nota = it.take(limite) },
                    label = { Text("Comentarios") },
                    minLines = 3,
                    maxLines = 6,
                    supportingText = { Text("${nota.length} / $limite caracteres") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Campo con sugerencias",
                componente = "ExposedDropdownMenuBox (editable)",
                descripcion = "Muestra opciones que coinciden con lo que se va escribiendo, de modo " +
                    "que el usuario puede completar el dato con un toque y evitar errores de captura.",
            ) {
                var texto by rememberSaveable { mutableStateOf("") }
                var expandido by rememberSaveable { mutableStateOf(false) }
                val opciones = ESTADOS.filter { it.contains(texto, ignoreCase = true) }.take(6)
                ExposedDropdownMenuBox(
                    expanded = expandido && opciones.isNotEmpty(),
                    onExpandedChange = { expandido = it },
                ) {
                    OutlinedTextField(
                        value = texto,
                        onValueChange = {
                            texto = it
                            expandido = true
                        },
                        label = { Text("Estado de la República") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                        singleLine = true,
                        modifier = Modifier
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable)
                            .fillMaxWidth(),
                    )
                    ExposedDropdownMenu(
                        expanded = expandido && opciones.isNotEmpty(),
                        onDismissRequest = { expandido = false },
                    ) {
                        opciones.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    texto = opcion
                                    expandido = false
                                },
                            )
                        }
                    }
                }
                TextoResultado(
                    if (texto in ESTADOS) "Seleccionaste: $texto" else "${opciones.size} sugerencias disponibles",
                )
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Barra de búsqueda",
                componente = "DockedSearchBar",
                descripcion = "Campo especializado para buscar contenido. Al activarse despliega " +
                    "resultados filtrados en tiempo real y ofrece un botón para limpiar la consulta.",
            ) {
                var consulta by rememberSaveable { mutableStateOf("") }
                var expandido by rememberSaveable { mutableStateOf(false) }
                var elegido by rememberSaveable { mutableStateOf("") }
                val resultados = COMPONENTES.filter { it.contains(consulta, ignoreCase = true) }
                DockedSearchBar(
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = consulta,
                            onQueryChange = { consulta = it },
                            onSearch = { expandido = false },
                            expanded = expandido,
                            onExpandedChange = { expandido = it },
                            placeholder = { Text("Buscar componente…") },
                            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                            trailingIcon = {
                                if (consulta.isNotEmpty()) {
                                    IconButton(onClick = { consulta = "" }) {
                                        Icon(Icons.Filled.Clear, contentDescription = "Limpiar búsqueda")
                                    }
                                }
                            },
                        )
                    },
                    expanded = expandido,
                    onExpandedChange = { expandido = it },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column {
                        if (resultados.isEmpty()) {
                            ListItem(headlineContent = { Text("Sin resultados para “$consulta”") })
                        }
                        resultados.take(5).forEach { resultado ->
                            ListItem(
                                headlineContent = { Text(resultado) },
                                leadingContent = { Icon(Icons.Filled.Search, contentDescription = null) },
                                modifier = Modifier.clickable {
                                    consulta = resultado
                                    elegido = resultado
                                    expandido = false
                                },
                            )
                        }
                    }
                }
                TextoResultado(
                    if (elegido.isEmpty()) "${resultados.size} de ${COMPONENTES.size} componentes coinciden"
                    else "Abriste el resultado: $elegido",
                )
            }
        }

        item {
            ElementoCatalogo(
                nombre = "Conexión con la Sección 4",
                componente = "OutlinedTextField + Button + ViewModel compartido",
                descripcion = "Lo que captures aquí se agrega al inicio de la lista vertical de la " +
                    "Sección 4. Ambas pantallas comparten el mismo ViewModel.",
            ) {
                val foco = LocalFocusManager.current
                var titulo by rememberSaveable { mutableStateOf("") }
                var descripcion by rememberSaveable { mutableStateOf("") }
                fun agregar() {
                    if (titulo.isBlank()) return
                    vm.agregar(titulo, descripcion)
                    avisos.snackbar("“${titulo.trim()}” se agregó a la Sección 4", "Ver", onIrALista)
                    titulo = ""
                    descripcion = ""
                    foco.clearFocus()
                }
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título del elemento *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción (opcional)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { agregar() }),
                    modifier = Modifier.fillMaxWidth(),
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Button(onClick = { agregar() }, enabled = titulo.isNotBlank()) {
                        Icon(Icons.AutoMirrored.Filled.PlaylistAdd, contentDescription = null, modifier = Modifier.size(18.dp))
                        Text("  Agregar a la lista")
                    }
                    Text(
                        "${vm.elementos.size} elementos",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}
