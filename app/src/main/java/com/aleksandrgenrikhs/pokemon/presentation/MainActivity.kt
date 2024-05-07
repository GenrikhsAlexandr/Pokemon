package com.aleksandrgenrikhs.pokemon.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavInflater
import androidx.navigation.fragment.NavHostFragment
import com.aleksandrgenrikhs.pokemon.PokemonApp
import com.aleksandrgenrikhs.pokemon.R
import com.aleksandrgenrikhs.pokemon.databinding.ActivityMainBinding
import com.aleksandrgenrikhs.pokemon.viewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.main_activity_nav_host) as NavHostFragment
    }

    private val navController: NavController by lazy { navHostFragment.navController }
    private val graphInflater: NavInflater by lazy { navController.navInflater }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        defineStartDestination()
    }

    private fun defineStartDestination() {
        val navGraph = graphInflater.inflate(R.navigation.app_graph)
        navController.graph = navGraph
        navController.navigate(R.id.mainFragment)
    }
}