package com.javiergamez.catalogoui.compose

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.javiergamez.catalogoui.compose.navegacion.AppCatalogo
import com.javiergamez.catalogoui.compose.tema.CatalogoTheme
import java.util.Locale

class MainActivity : ComponentActivity() {

    // Fuerza el idioma español para que los selectores de fecha/hora y
    // los textos del sistema también aparezcan en español.
    override fun attachBaseContext(newBase: Context) {
        val locale = Locale.forLanguageTag("es-MX")
        Locale.setDefault(locale)
        val config = Configuration(newBase.resources.configuration)
        config.setLocale(locale)
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CatalogoTheme {
                AppCatalogo()
            }
        }
    }
}
