package com.example.test

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.test.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

/**
 * Activity utama yang menjadi host Navigation + Fragment.
 *
 * UI utama aplikasi sebenarnya ada di:
 *  - FirstFragment  -> pengantar + info kelompok
 *  - SecondFragment -> host Jetpack Compose (ProfileApp di Scaffold.kt)
 */
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set Toolbar sebagai ActionBar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Profil Kelompok STI"

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // FAB bawaan template dimatikan,
        // karena FAB utama ada di dalam Scaffold (ProfileApp).
        binding.fab.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Menu pojok kanan atas (⋮)
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                Snackbar.make(
                    binding.root,
                    "Tugas Praktikum Sesi 6 — Scaffold + Compose",
                    Snackbar.LENGTH_LONG
                ).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
