package com.aleksandrgenrikhs.pokemon.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aleksandrgenrikhs.pokemon.domain.Page
import com.aleksandrgenrikhs.pokemon.domain.PokemonInteractor
import com.aleksandrgenrikhs.pokemon.utils.ResultState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainViewModel
@Inject constructor(
    private val interator: PokemonInteractor,
) : ViewModel() {

    private val _pokemon: MutableStateFlow<Page?> = MutableStateFlow(null)
    val pokemon: StateFlow<Page?> = _pokemon

    private val _isProgressBarVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isProgressBarVisible: StateFlow<Boolean> = _isProgressBarVisible

    private val _isNextButtonVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isNextButtonVisible: StateFlow<Boolean> = _isNextButtonVisible

    private val _isPreviousButtonVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isPreviousButtonVisible: StateFlow<Boolean> = _isPreviousButtonVisible


    val toastMessageError: MutableSharedFlow<ResultState.Error> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        getPokemon(0)
    }

    fun getPokemon(page: Int?) {
        viewModelScope.launch {
            _isProgressBarVisible.value = true
            when (val pokemon = interator.getPokemon(page)) {
                is ResultState.Error -> {
                    toastMessageError.tryEmit(ResultState.Error(pokemon.message))
                }

                is ResultState.Success -> {
                    _pokemon.value = pokemon.data
                    _isNextButtonVisible.value = pokemon.data?.next != null
                    _isPreviousButtonVisible.value = pokemon.data?.previous != null
                }
            }
            _isProgressBarVisible.value = false
        }
    }
}