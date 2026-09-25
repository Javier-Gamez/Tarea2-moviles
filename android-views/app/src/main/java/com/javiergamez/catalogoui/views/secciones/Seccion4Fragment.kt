package com.javiergamez.catalogoui.views.secciones

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.R as MaterialR
import com.google.android.material.color.MaterialColors
import com.google.android.material.tabs.TabLayoutMediator
import com.javiergamez.catalogoui.views.R
import com.javiergamez.catalogoui.views.componentes.dp
import com.javiergamez.catalogoui.views.componentes.mostrar
import com.javiergamez.catalogoui.views.componentes.snackbar
import com.javiergamez.catalogoui.views.componentes.toast
import com.javiergamez.catalogoui.views.databinding.FragmentSeccion4Binding
import com.javiergamez.catalogoui.views.databinding.ItemIconoTextoBinding
import com.javiergamez.catalogoui.views.datos.CatalogoViewModel
import com.javiergamez.catalogoui.views.navegacion.Seccion
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val COLORES = listOf(
    "#E57373", "#F06292", "#BA68C8", "#9575CD", "#7986CB", "#64B5F6",
    "#4DD0E1", "#4DB6AC", "#81C784", "#DCE775", "#FFD54F", "#FF8A65",
).map(Color::parseColor)

private val CORREOS = listOf(
    "Recordatorio de clase", "Entrega de la práctica", "Boletín semanal",
    "Invitación a conferencia", "Confirmación de registro", "Promoción de temporada",
)

private val FILAS_AGRUPADAS = listOf(
    FilaAgrupada.Encabezado("Favoritos"),
    FilaAgrupada.Contacto("Ana Martínez", "Diseñadora"),
    FilaAgrupada.Contacto("Bruno Díaz", "Desarrollador"),
    FilaAgrupada.Anuncio("Tus favoritos se sincronizan en todos tus dispositivos."),
    FilaAgrupada.Encabezado("Compañeros"),
    FilaAgrupada.Contacto("Carla Núñez", "Grupo 7CM1"),
    FilaAgrupada.Contacto("Diego Ramos", "Grupo 7CM1"),
    FilaAgrupada.Contacto("Elena Soto", "Grupo 7CM2"),
    FilaAgrupada.Anuncio("Invita a tus compañeros a probar la aplicación."),
    FilaAgrupada.Encabezado("Profesores"),
    FilaAgrupada.Contacto("Fernando Gil", "Aplicaciones Móviles"),
    FilaAgrupada.Contacto("Gabriela Ortiz", "Ingeniería de Software"),
)

class Seccion4Fragment : Fragment(R.layout.fragment_seccion4) {

    private val vm: CatalogoViewModel by activityViewModels()

    // Estado de las demostraciones; se conserva al volver desde el detalle.
    private val correos = CORREOS.toMutableList()
    private val noticias = mutableListOf("Noticia 3 · inicial", "Noticia 2 · inicial", "Noticia 1 · inicial")
    private var contadorNoticias = 3
    private val tareas = mutableListOf("Repasar apuntes", "Subir la tarea")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentSeccion4Binding.bind(view)
        b.encabezado.mostrar(Seccion.LISTAS)

        configurarListaVertical(b)
        configurarCuadricula(b)

        b.listaAgrupada.layoutManager = LinearLayoutManager(requireContext())
        b.listaAgrupada.adapter = AdaptadorAgrupado(FILAS_AGRUPADAS) { toast("Contacto: $it") }

        configurarCorreos(b)
        configurarNoticias(b)
        configurarTareas(b)
        configurarPestanas(b)
    }

    private fun configurarListaVertical(b: FragmentSeccion4Binding) {
        val adaptador = AdaptadorElementos { elemento ->
            findNavController().navigate(R.id.detalleFragment, bundleOf("id" to elemento.id))
        }
        b.listaVertical.layoutManager = LinearLayoutManager(requireContext())
        b.listaVertical.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        b.listaVertical.adapter = adaptador
        vm.elementos.observe(viewLifecycleOwner) { lista ->
            adaptador.submitList(lista)
            b.totalLista.text = "${lista.size} elementos"
        }
    }

    private fun configurarCuadricula(b: FragmentSeccion4Binding) {
        b.cuadricula.layoutManager = GridLayoutManager(requireContext(), 3)
        b.cuadricula.adapter = AdaptadorCuadricula(COLORES) { seleccion ->
            b.resultadoCuadricula.text = "Celdas seleccionadas: ${seleccion.size} de ${COLORES.size}"
        }
        b.resultadoCuadricula.text = "Celdas seleccionadas: 0 de ${COLORES.size}"
    }

    private fun configurarCorreos(b: FragmentSeccion4Binding) {
        val adaptador = AdaptadorTextos(R.drawable.ic_email, "Desliza para eliminar")
        b.listaCorreos.layoutManager = LinearLayoutManager(requireContext())
        b.listaCorreos.adapter = adaptador

        fun refrescar() {
            adaptador.submitList(correos.toList())
            b.bandejaVacia.isVisible = correos.isEmpty()
        }

        val fondo = Paint().apply { color = MaterialColors.getColor(b.root, MaterialR.attr.colorErrorContainer) }
        val papelera = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)!!.mutate().apply {
            setTint(MaterialColors.getColor(b.root, MaterialR.attr.colorOnErrorContainer))
        }
        val deslizar = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(rv: RecyclerView, a: RecyclerView.ViewHolder, c: RecyclerView.ViewHolder) = false

            override fun onSwiped(fila: RecyclerView.ViewHolder, direccion: Int) {
                val posicion = fila.bindingAdapterPosition
                val correo = correos.removeAt(posicion)
                refrescar()
                snackbar("Correo eliminado", "Deshacer") {
                    if (correo !in correos) {
                        correos.add(posicion.coerceAtMost(correos.size), correo)
                        refrescar()
                    }
                }
            }

            // Dibuja el fondo rojo con la papelera detrás de la fila mientras se desliza.
            override fun onChildDraw(
                c: Canvas, rv: RecyclerView, fila: RecyclerView.ViewHolder,
                dX: Float, dY: Float, estado: Int, activo: Boolean,
            ) {
                val v = fila.itemView
                val margen = v.dp(20)
                val mitad = (v.height - papelera.intrinsicHeight) / 2
                if (dX > 0) {
                    c.drawRect(v.left.toFloat(), v.top.toFloat(), v.left + dX, v.bottom.toFloat(), fondo)
                    papelera.setBounds(v.left + margen, v.top + mitad, v.left + margen + papelera.intrinsicWidth, v.bottom - mitad)
                } else if (dX < 0) {
                    c.drawRect(v.right + dX, v.top.toFloat(), v.right.toFloat(), v.bottom.toFloat(), fondo)
                    papelera.setBounds(v.right - margen - papelera.intrinsicWidth, v.top + mitad, v.right - margen, v.bottom - mitad)
                }
                if (dX != 0f) papelera.draw(c)
                super.onChildDraw(c, rv, fila, dX, dY, estado, activo)
            }
        }
        ItemTouchHelper(deslizar).attachToRecyclerView(b.listaCorreos)

        b.botonRestaurar.setOnClickListener {
            correos.clear()
            correos.addAll(CORREOS)
            refrescar()
        }
        refrescar()
    }

    private fun configurarNoticias(b: FragmentSeccion4Binding) {
        val adaptador = AdaptadorTextos(R.drawable.ic_newspaper)
        b.listaNoticias.layoutManager = LinearLayoutManager(requireContext())
        b.listaNoticias.adapter = adaptador
        val formato = SimpleDateFormat("HH:mm:ss", Locale.forLanguageTag("es-MX"))

        fun refrescar() {
            adaptador.submitList(noticias.toList()) { b.listaNoticias.scrollToPosition(0) }
            b.resultadoNoticias.text = "Arrastra la lista hacia abajo. Total: ${noticias.size} noticias"
        }

        b.refrescar.setColorSchemeColors(MaterialColors.getColor(b.root, androidx.appcompat.R.attr.colorPrimary))
        b.refrescar.setOnRefreshListener {
            viewLifecycleOwner.lifecycleScope.launch {
                delay(1500)
                contadorNoticias++
                noticias.add(0, "Noticia $contadorNoticias · ${formato.format(Date())}")
                refrescar()
                b.refrescar.isRefreshing = false
            }
        }
        refrescar()
    }

    private fun configurarTareas(b: FragmentSeccion4Binding) {
        fun refrescar() {
            b.listaTareas.removeAllViews()
            tareas.forEach { tarea ->
                val fila = ItemIconoTextoBinding.inflate(layoutInflater, b.listaTareas, true)
                fila.root.background = null
                fila.icono.setImageResource(R.drawable.ic_check_circle)
                fila.linea1.text = tarea
            }
            b.contenedorTareas.isVisible = tareas.isNotEmpty()
            b.estadoVacio.isVisible = tareas.isEmpty()
        }
        b.botonVaciar.setOnClickListener { tareas.clear(); refrescar() }
        b.botonEjemplo.setOnClickListener {
            tareas.addAll(listOf("Repasar apuntes", "Subir la tarea", "Leer capítulo 3"))
            refrescar()
        }
        refrescar()
    }

    private fun configurarPestanas(b: FragmentSeccion4Binding) {
        val paginas = listOf(
            Pagina("Recientes", R.drawable.ic_history, "Archivos abiertos hoy"),
            Pagina("Favoritos", R.drawable.ic_star, "Archivos marcados con estrella"),
            Pagina("Archivados", R.drawable.ic_archive, "Archivos guardados para después"),
        )
        b.paginador.adapter = AdaptadorPaginas(paginas)
        TabLayoutMediator(b.pestanas, b.paginador) { pestana, posicion ->
            pestana.text = paginas[posicion].titulo
            pestana.setIcon(paginas[posicion].icono)
        }.attach()
        b.paginador.registerOnPageChangeCallback(object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(posicion: Int) {
                b.resultadoPestana.text = "Pestaña actual: ${paginas[posicion].titulo}"
            }
        })
    }
}
