package com.aleksandrgenrikhs.pokemon

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import com.aleksandrgenrikhs.pokemon.di.AppComponent
import com.aleksandrgenrikhs.pokemon.di.ComponentProvider

class PokemonApp : Application(), ComponentProvider {
    val appComponent: AppComponent by lazy {
        AppComponent.init(this)
    }

    override fun componentProvider() = appComponent
}

val Context.app: PokemonApp get() = applicationContext as PokemonApp
val Fragment.app: PokemonApp get() = requireActivity().app

val Context.viewModelFactory get() = app.appComponent.viewModelFactory
val Fragment.viewModelFactory get() = app.appComponent.viewModelFactory