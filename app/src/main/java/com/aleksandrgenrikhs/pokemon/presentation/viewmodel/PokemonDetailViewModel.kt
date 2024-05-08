package com.aleksandrgenrikhs.pokemon.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aleksandrgenrikhs.pokemon.ResultState
import com.aleksandrgenrikhs.pokemon.domain.PokemonDetail
import com.aleksandrgenrikhs.pokemon.domain.PokemonInteractor
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PokemonDetailViewModel
@Inject constructor(
    pokemonId: Int,
    private val interactor: PokemonInteractor,
) : ViewModel() {

    private val _pokemonDetail: MutableStateFlow<PokemonDetail?> = MutableStateFlow(null)
    val pokemonDetail: StateFlow<PokemonDetail?> = _pokemonDetail

    private val _isProgressBarVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isProgressBarVisible: StateFlow<Boolean> = _isProgressBarVisible

    val toastMessageError: MutableSharedFlow<ResultState.Error> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        viewModelScope.launch {
            _isProgressBarVisible.value = true
            when (val pokemon = interactor.getDetailPokemon(pokemonId)
            ) {
                is ResultState.Error -> {
                    toastMessageError.tryEmit(ResultState.Error(pokemon.message))
                }

                is ResultState.Success -> {
                    _pokemonDetail.value = pokemon.data
                }
            }
            _isProgressBarVisible.value = false
        }
    }

    fun cries() {
        viewModelScope.launch {
            val url = ("${pokemonDetail.value?.cries}")
            when (val cries = interactor.getCries(url)) {
                is ResultState.Success -> {
                    cries.data
                }

                is ResultState.Error -> {
                    interactor.playerDestroy()
                    toastMessageError.tryEmit(ResultState.Error(cries.message))
                }
            }
        }
    }

    fun destroyPlayer() {
        viewModelScope.launch {
            interactor.playerDestroy()
        }
    }
}