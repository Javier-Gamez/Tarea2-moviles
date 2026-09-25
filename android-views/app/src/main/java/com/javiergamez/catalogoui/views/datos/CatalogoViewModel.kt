package com.javiergamez.catalogoui.views.datos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
 * Estado compartido entre secciones. Los Fragments lo obtienen con
 * `activityViewModels()`, así todos usan la misma instancia.
 */
class CatalogoViewModel : ViewModel() {

    private var siguienteId = 1

    private val _elementos = MutableLiveData<List<ElementoLista>>()
    val elementos: LiveData<List<ElementoLista>> = _elementos

    private val _tamanoTexto = MutableLiveData(TamanoTexto.MEDIANO)
    val tamanoTexto: LiveData<TamanoTexto> = _tamanoTexto

    init {
        _elementos.value = CIUDADES.map { (ciudad, estado) ->
            ElementoLista(siguienteId++, ciudad, "Ciudad ubicada en $estado.")
        }
    }

    private val lista: List<ElementoLista> get() = _elementos.value.orEmpty()

    fun elegirTamano(tamano: TamanoTexto) {
        _tamanoTexto.value = tamano
    }

    /** Conexión Sección 1 → Sección 4: agrega un elemento capturado por el usuario. */
    fun agregar(titulo: String, descripcion: String) {
        val texto = descripcion.trim().ifBlank { "Elemento agregado desde la Sección 1." }
        _elementos.value = listOf(ElementoLista(siguienteId++, titulo.trim(), texto, deUsuario = true)) + lista
    }

    fun buscar(id: Int): ElementoLista? = lista.find { it.id == id }

    fun eliminar(id: Int) {
        _elementos.value = lista.filterNot { it.id == id }
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
