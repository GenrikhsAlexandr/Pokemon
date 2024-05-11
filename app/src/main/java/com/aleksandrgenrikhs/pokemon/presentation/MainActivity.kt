package com.aleksandrgenrikhs.pokemon.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavInflater
import androidx.navigation.fragment.NavHostFragment
import com.aleksandrgenrikhs.pokemon.R
import com.aleksandrgenrikhs.pokemon.databinding.ActivityMainBinding
import com.aleksandrgenrikhs.pokemon.presentation.viewmodel.MainViewModel
import com.aleksandrgenrikhs.pokemon.viewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels { viewModelFactory }
    private lateinit var binding: ActivityMainBinding

    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.main_activity_nav_host) as NavHostFragment
    }

    private val navController: NavController by lazy { navHostFragment.navController }
    private val graphInflater: NavInflater by lazy { navController.navInflater }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition { !viewModel.isReady.value }
        }
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getFirstPage()
        refresh()
        subscribe()
        startMainFragment()

    }

   private fun subscribe(){
      viewModel.viewModelScope.launch {
          viewModel.isErrorLayoutVisible.collect{ isVisible ->
              binding.errorLayout.isVisible = isVisible
          }
      }
   }

    private fun startMainFragment() {
        val navGraph = graphInflater.inflate(R.navigation.app_graph)
        navController.graph = navGraph
        navController.navigate(R.id.mainFragment)
    }

    private fun refresh(){
        binding.refreshButton.setOnClickListener {
            viewModel.getFirstPage()
        }
    }
}