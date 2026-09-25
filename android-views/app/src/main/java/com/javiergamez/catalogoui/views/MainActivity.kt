package com.javiergamez.catalogoui.views

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.javiergamez.catalogoui.views.databinding.ActivityMainBinding
import com.javiergamez.catalogoui.views.navegacion.Seccion

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var nav: NavController
    private lateinit var configuracionBarra: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val host = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        nav = host.navController

        // La pantalla principal y las seis secciones son destinos de primer nivel:
        // muestran el ícono del menú lateral en lugar de la flecha de regresar.
        val destinosPrincipales = setOf(R.id.inicioFragment) + Seccion.entries.map { it.destino }
        configuracionBarra = AppBarConfiguration(destinosPrincipales, binding.drawer)
        setupActionBarWithNavController(nav, configuracionBarra)
        binding.navView.setupWithNavController(nav)
        nav.addOnDestinationChangedListener { _, _, _ -> invalidateOptionsMenu() }

        // Deja espacio para la barra de navegación del sistema y el teclado.
        ViewCompat.setOnApplyWindowInsetsListener(binding.navHost) { vista, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val teclado = insets.getInsets(WindowInsetsCompat.Type.ime())
            vista.updatePadding(bottom = maxOf(barras.bottom, teclado.bottom))
            insets
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_barra, menu)
        menu.findItem(R.id.accion_inicio).isVisible = nav.currentDestination?.id != R.id.inicioFragment
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.accion_inicio) {
            nav.popBackStack(R.id.inicioFragment, false)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean =
        nav.navigateUp(configuracionBarra) || super.onSupportNavigateUp()
}
