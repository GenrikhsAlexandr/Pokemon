package com.aleksandrgenrikhs.pokemon.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aleksandrgenrikhs.pokemon.ResultState
import com.aleksandrgenrikhs.pokemon.domain.Offset
import com.aleksandrgenrikhs.pokemon.domain.Pokemon
import com.aleksandrgenrikhs.pokemon.domain.PokemonInteractor
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainViewModel
@Inject constructor(
    val interator: PokemonInteractor,
) : ViewModel() {

    private val _pokemon: MutableStateFlow<List<Pokemon>?> = MutableStateFlow(emptyList())
    val pokemon: StateFlow<List<Pokemon>?> = _pokemon

    val offset: MutableStateFlow<Offset?> = MutableStateFlow(null)

    val toastMessageError: MutableSharedFlow<ResultState.Error> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        getPokemon(0)
    }

    fun getPokemon(page: Int?) {
        viewModelScope.launch {
            when (val pokemon = interator.getPokemon(page)) {
                is ResultState.Error -> {
                    toastMessageError.tryEmit(ResultState.Error(pokemon.message))
                }

                is ResultState.Success -> {
                    _pokemon.value = pokemon.data
                    offset.value = interator.getOffset()
                }
            }
        }
    }
}