package com.javiergamez.catalogoui.views.navegacion

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import com.javiergamez.catalogoui.views.R

/** Las seis secciones del catálogo; cada una es un Fragment del grafo de navegación. */
enum class Seccion(
    @IdRes val destino: Int,
    val numero: Int,
    val titulo: String,
    val resumen: String,
    @DrawableRes val icono: Int,
) {
    ENTRADA_TEXTO(R.id.seccion1Fragment, 1, "Entrada de texto", "Campos para capturar información escrita.", R.drawable.ic_text_fields),
    BOTONES(R.id.seccion2Fragment, 2, "Botones y acciones", "Controles que ejecutan acciones al pulsarse.", R.drawable.ic_smart_button),
    SELECCION(R.id.seccion3Fragment, 3, "Elementos de selección", "Controles para elegir opciones y valores.", R.drawable.ic_check_box),
    LISTAS(R.id.seccion4Fragment, 4, "Listas y colecciones", "Formas de mostrar conjuntos de elementos.", R.drawable.ic_list),
    INFORMACION(R.id.seccion5Fragment, 5, "Información y retroalimentación", "Elementos que comunican estados y resultados.", R.drawable.ic_info),
    CONTENEDORES(R.id.seccion6Fragment, 6, "Contenedores y estructura", "Distribuciones que organizan la pantalla.", R.drawable.ic_dashboard),
}
