package com.javiergamez.catalogoui.views.secciones

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.javiergamez.catalogoui.views.componentes.dp
import com.javiergamez.catalogoui.views.databinding.ItemAnuncioBinding
import com.javiergamez.catalogoui.views.databinding.ItemCeldaBinding
import com.javiergamez.catalogoui.views.databinding.ItemElementoBinding
import com.javiergamez.catalogoui.views.databinding.ItemEncabezadoBinding
import com.javiergamez.catalogoui.views.databinding.ItemIconoTextoBinding
import com.javiergamez.catalogoui.views.databinding.PaginaPestanaBinding
import com.javiergamez.catalogoui.views.datos.ElementoLista

/** Lista vertical principal (compartida con la Sección 1). */
class AdaptadorElementos(
    private val alTocar: (ElementoLista) -> Unit,
) : ListAdapter<ElementoLista, AdaptadorElementos.Fila>(DIFERENCIAS) {

    class Fila(val b: ItemElementoBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Fila(ItemElementoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(fila: Fila, posicion: Int) {
        val e = getItem(posicion)
        fila.b.avatar.text = e.titulo.take(1).uppercase()
        fila.b.titulo.text = e.titulo
        fila.b.descripcion.text = e.descripcion
        fila.b.etiquetaNuevo.isVisible = e.deUsuario
        fila.b.root.setOnClickListener { alTocar(e) }
    }

    private companion object {
        val DIFERENCIAS = object : DiffUtil.ItemCallback<ElementoLista>() {
            override fun areItemsTheSame(a: ElementoLista, b: ElementoLista) = a.id == b.id
            override fun areContentsTheSame(a: ElementoLista, b: ElementoLista) = a == b
        }
    }
}

/** Celdas de colores de la cuadrícula; se pueden seleccionar varias. */
class AdaptadorCuadricula(
    private val colores: List<Int>,
    private val alCambiar: (Set<Int>) -> Unit,
) : RecyclerView.Adapter<AdaptadorCuadricula.Celda>() {

    private val seleccionadas = mutableSetOf<Int>()

    class Celda(val b: ItemCeldaBinding) : RecyclerView.ViewHolder(b.root)

    override fun getItemCount() = colores.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Celda(ItemCeldaBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(celda: Celda, posicion: Int) {
        val activa = posicion in seleccionadas
        celda.b.root.setCardBackgroundColor(colores[posicion])
        celda.b.root.strokeWidth = if (activa) celda.b.root.dp(3) else 0
        celda.b.textoCelda.text = "${posicion + 1}"
        celda.b.textoCelda.isVisible = !activa
        celda.b.iconoCelda.isVisible = activa
        celda.b.root.setOnClickListener {
            if (!seleccionadas.remove(posicion)) seleccionadas.add(posicion)
            notifyItemChanged(posicion)
            alCambiar(seleccionadas)
        }
    }
}

/** Filas de la lista con encabezados: tres tipos distintos de vista. */
sealed interface FilaAgrupada {
    data class Encabezado(val texto: String) : FilaAgrupada
    data class Contacto(val nombre: String, val detalle: String) : FilaAgrupada
    data class Anuncio(val texto: String) : FilaAgrupada
}

class AdaptadorAgrupado(
    private val filas: List<FilaAgrupada>,
    private val alTocarContacto: (String) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private class VistaEncabezado(val b: ItemEncabezadoBinding) : RecyclerView.ViewHolder(b.root)
    private class VistaContacto(val b: ItemIconoTextoBinding) : RecyclerView.ViewHolder(b.root)
    private class VistaAnuncio(val b: ItemAnuncioBinding) : RecyclerView.ViewHolder(b.root)

    override fun getItemCount() = filas.size

    override fun getItemViewType(posicion: Int) = when (filas[posicion]) {
        is FilaAgrupada.Encabezado -> TIPO_ENCABEZADO
        is FilaAgrupada.Contacto -> TIPO_CONTACTO
        is FilaAgrupada.Anuncio -> TIPO_ANUNCIO
    }

    override fun onCreateViewHolder(parent: ViewGroup, tipo: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (tipo) {
            TIPO_ENCABEZADO -> VistaEncabezado(ItemEncabezadoBinding.inflate(inflater, parent, false))
            TIPO_CONTACTO -> VistaContacto(ItemIconoTextoBinding.inflate(inflater, parent, false))
            else -> VistaAnuncio(ItemAnuncioBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(vista: RecyclerView.ViewHolder, posicion: Int) {
        when (val fila = filas[posicion]) {
            is FilaAgrupada.Encabezado -> (vista as VistaEncabezado).b.textoEncabezado.text = fila.texto
            is FilaAgrupada.Anuncio -> (vista as VistaAnuncio).b.textoAnuncio.text = fila.texto
            is FilaAgrupada.Contacto -> with((vista as VistaContacto).b) {
                icono.isVisible = false
                avatar.isVisible = true
                avatar.text = fila.nombre.take(1)
                linea1.text = fila.nombre
                linea2.isVisible = true
                linea2.text = fila.detalle
                root.setOnClickListener { alTocarContacto(fila.nombre) }
            }
        }
    }

    private companion object {
        const val TIPO_ENCABEZADO = 0
        const val TIPO_CONTACTO = 1
        const val TIPO_ANUNCIO = 2
    }
}

/** Filas de texto con un ícono (correos y noticias). */
class AdaptadorTextos(
    @param:DrawableRes private val icono: Int,
    private val subtitulo: String? = null,
) : ListAdapter<String, AdaptadorTextos.Fila>(DIFERENCIAS) {

    class Fila(val b: ItemIconoTextoBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Fila(ItemIconoTextoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(fila: Fila, posicion: Int) {
        fila.b.icono.setImageResource(icono)
        fila.b.linea1.text = getItem(posicion)
        fila.b.linea2.isVisible = subtitulo != null
        fila.b.linea2.text = subtitulo
    }

    private companion object {
        val DIFERENCIAS = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(a: String, b: String) = a == b
            override fun areContentsTheSame(a: String, b: String) = a == b
        }
    }
}

/** Páginas de las pestañas deslizables. */
data class Pagina(val titulo: String, @param:DrawableRes val icono: Int, val texto: String)

class AdaptadorPaginas(private val paginas: List<Pagina>) : RecyclerView.Adapter<AdaptadorPaginas.Vista>() {

    class Vista(val b: PaginaPestanaBinding) : RecyclerView.ViewHolder(b.root)

    override fun getItemCount() = paginas.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Vista(PaginaPestanaBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(vista: Vista, posicion: Int) {
        val p = paginas[posicion]
        vista.b.iconoPagina.setImageResource(p.icono)
        vista.b.tituloPagina.text = p.titulo
        vista.b.textoPagina.text = p.texto
    }
}
