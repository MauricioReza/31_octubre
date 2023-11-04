package com.amaurypm.videogamesrf.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.amaurypm.videogamesrf.R
import com.amaurypm.videogamesrf.databinding.ActivityMainBinding
import com.amaurypm.videogamesrf.ui.fragments.HomeFragment
import com.amaurypm.videogamesrf.ui.fragments.InformationFragment
import com.amaurypm.videogamesrf.ui.fragments.MyLibraryFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavView: BottomNavigationView

    fun navigateToMainFragment() {
        val fragment = HomeFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null) // Agregar transacción a la pila
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa bottomNavView antes de usarlo
        bottomNavView = findViewById(R.id.bottomNavView)

        bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Reemplaza el fragmento actual con HomeFragment
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment())
                        .addToBackStack(null) // Agregar transacción a la pila
                        .commit()
                }
                R.id.nav_book_list -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, MyLibraryFragment())
                        .addToBackStack(null) // Agregar transacción a la pila
                        .commit()
                }
                R.id.nav_scan -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, InformationFragment())
                        .addToBackStack(null) // Agregar transacción a la pila
                        .commit()
                }
            }
            true
        }

        // Configura un Listener para controlar la visibilidad del BottomNavigationView
        supportFragmentManager.addOnBackStackChangedListener {
            // Obtén el fragmento actual
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

            // Establece la visibilidad del BottomNavigationView según el fragmento actual
            bottomNavView.visibility = when (currentFragment) {
                is HomeFragment, is MyLibraryFragment, is InformationFragment -> View.VISIBLE
                else -> View.GONE
            }
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }
    }
}