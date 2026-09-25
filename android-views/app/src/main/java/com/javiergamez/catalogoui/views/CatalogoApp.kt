package com.javiergamez.catalogoui.views

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

class CatalogoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Fuerza el español para que los selectores de fecha/hora y los textos
        // del sistema aparezcan en el mismo idioma que la app.
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("es-MX"))
    }
}
