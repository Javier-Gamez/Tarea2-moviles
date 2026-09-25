package com.javiergamez.catalogoui.views.secciones

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

/** Marcador temporal mientras se implementa cada sección. */
class EnConstruccionFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        TextView(requireContext()).apply {
            text = "Sección en construcción"
            gravity = Gravity.CENTER
        }
}
