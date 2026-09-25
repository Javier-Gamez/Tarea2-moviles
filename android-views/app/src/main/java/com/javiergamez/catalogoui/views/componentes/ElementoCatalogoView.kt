package com.javiergamez.catalogoui.views.componentes

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import com.google.android.material.card.MaterialCardView
import com.javiergamez.catalogoui.views.R

/**
 * Tarjeta que documenta un elemento del catálogo: nombre, componente usado,
 * explicación breve y una demostración interactiva.
 *
 * Los hijos declarados dentro de esta vista en XML se colocan automáticamente
 * en el área de demostración, debajo del separador.
 */
class ElementoCatalogoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.materialCardViewElevatedStyle,
) : MaterialCardView(context, attrs, defStyleAttr) {

    private var estructuraLista = false
    private val contenedorDemo: LinearLayout

    init {
        LayoutInflater.from(context).inflate(R.layout.vista_elemento_catalogo, this, true)
        contenedorDemo = findViewById(R.id.contenedor_demo)
        estructuraLista = true

        context.withStyledAttributes(attrs, R.styleable.ElementoCatalogoView) {
            findViewById<TextView>(R.id.nombre).text = getString(R.styleable.ElementoCatalogoView_nombre)
            findViewById<TextView>(R.id.componente).text = getString(R.styleable.ElementoCatalogoView_componente)
            findViewById<TextView>(R.id.descripcion).text = getString(R.styleable.ElementoCatalogoView_descripcion)
        }
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (estructuraLista) {
            contenedorDemo.addView(child, params)
        } else {
            super.addView(child, index, params)
        }
    }
}
