package com.aleksandrgenrikhs.pokemon.presentation.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aleksandrgenrikhs.pokemon.domain.PokemonInteractor
import com.aleksandrgenrikhs.pokemon.presentation.viewmodel.PokemonDetailViewModel
import com.aleksandrgenrikhs.pokemon.utils.PokemonMediaPlayer
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class PokemonDetailViewModelFactory
@AssistedInject constructor(
    @Assisted private val pokemonId: Int,
    private val interactor: PokemonInteractor,
    private val mediaPlayer: PokemonMediaPlayer,
    private val application: Application,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PokemonDetailViewModel(pokemonId, interactor, mediaPlayer, application) as T
    }
}

@AssistedFactory
interface PokemonDetailViewModelAssistedFactory {

    fun create(
        @Assisted pokemonId: Int,
    ): PokemonDetailViewModelFactory
}