package com.aleksandrgenrikhs.pokemon.presentation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aleksandrgenrikhs.pokemon.domain.PokemonInteractor
import com.aleksandrgenrikhs.pokemon.presentation.viewmodel.PokemonDetailViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class PokemonDetailViewModelFactory
@AssistedInject constructor(
    @Assisted private val pokemonId: Int,
    private val interactor: PokemonInteractor,
    ) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PokemonDetailViewModel(pokemonId, interactor) as T
    }
}

@AssistedFactory
interface PokemonDetailViewModelAssistedFactory {

    fun create(
        @Assisted pokemonId: Int,
    ): PokemonDetailViewModelFactory
}