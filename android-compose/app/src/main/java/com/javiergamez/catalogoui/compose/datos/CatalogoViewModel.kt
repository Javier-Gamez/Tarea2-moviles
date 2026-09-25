package com.javiergamez.catalogoui.compose.datos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class ElementoLista(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val deUsuario: Boolean = false,
)

/** Preferencia elegida en la Sección 3 que modifica los textos de la Sección 5. */
enum class TamanoTexto(val etiqueta: String, val escala: Float) {
    PEQUENO("Pequeño", 0.85f),
    MEDIANO("Mediano", 1f),
    GRANDE("Grande", 1.25f),
}

/**
 * Estado compartido entre secciones. Vive mientras exista la actividad,
 * por lo que los datos capturados en una sección están disponibles en otra.
 */
class CatalogoViewModel : ViewModel() {

    private var siguienteId = 1

    val elementos = mutableStateListOf<ElementoLista>()

    var tamanoTexto by mutableStateOf(TamanoTexto.MEDIANO)

    init {
        CIUDADES.forEach { (ciudad, estado) ->
            elementos.add(ElementoLista(siguienteId++, ciudad, "Ciudad ubicada en $estado."))
        }
    }

    /** Conexión Sección 1 → Sección 4: agrega un elemento capturado por el usuario. */
    fun agregar(titulo: String, descripcion: String) {
        val texto = descripcion.ifBlank { "Elemento agregado desde la Sección 1." }
        elementos.add(0, ElementoLista(siguienteId++, titulo.trim(), texto.trim(), deUsuario = true))
    }

    fun buscar(id: Int): ElementoLista? = elementos.find { it.id == id }

    fun eliminar(id: Int) {
        elementos.removeAll { it.id == id }
    }

    companion object {
        val CIUDADES = listOf(
            "Ciudad de México" to "la Ciudad de México",
            "Guadalajara" to "Jalisco",
            "Monterrey" to "Nuevo León",
            "Puebla" to "Puebla",
            "Querétaro" to "Querétaro",
            "Mérida" to "Yucatán",
            "Oaxaca" to "Oaxaca",
            "Guanajuato" to "Guanajuato",
            "Morelia" to "Michoacán",
            "Veracruz" to "Veracruz",
            "Tijuana" to "Baja California",
            "León" to "Guanajuato",
            "Chihuahua" to "Chihuahua",
            "Zacatecas" to "Zacatecas",
            "San Luis Potosí" to "San Luis Potosí",
            "Toluca" to "el Estado de México",
        )
    }
}
