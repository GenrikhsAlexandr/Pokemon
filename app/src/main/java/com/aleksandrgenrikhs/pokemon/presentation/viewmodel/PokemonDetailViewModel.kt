package com.aleksandrgenrikhs.pokemon.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aleksandrgenrikhs.pokemon.R
import com.aleksandrgenrikhs.pokemon.domain.PokemonDetail
import com.aleksandrgenrikhs.pokemon.domain.PokemonInteractor
import com.aleksandrgenrikhs.pokemon.utils.PokemonMediaPlayer
import com.aleksandrgenrikhs.pokemon.utils.ResultState
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
    private val mediaPlayer: PokemonMediaPlayer,
    private val application: Application

) : ViewModel() {

    private val _pokemonDetail: MutableStateFlow<PokemonDetail?> = MutableStateFlow(null)
    val pokemonDetail: StateFlow<PokemonDetail?> = _pokemonDetail

    private val _isProgressBarVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isProgressBarVisible: StateFlow<Boolean> = _isProgressBarVisible

    private val _isCardViewVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isCardViewVisible: StateFlow<Boolean> = _isCardViewVisible

    val toastMessageError: MutableSharedFlow<Int> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        viewModelScope.launch {
            _isProgressBarVisible.value = true
            when (val result = interactor.getDetailPokemon(pokemonId)
            ) {
                is ResultState.Error -> {
                    _isCardViewVisible.value = false
                    if (!interactor.isNetWorkConnected()) {
                        toastMessageError.tryEmit(R.string.error_message)
                    } else {
                        toastMessageError.tryEmit(result.message)
                    }
                }

                is ResultState.Success -> {
                    _isCardViewVisible.value = true
                    _pokemonDetail.value = result.data
                }
            }
            _isProgressBarVisible.value = false
        }
    }

    fun cries() {
        val url = ("${pokemonDetail.value?.cries}")
        mediaPlayer.initPlayer(application, url)?.start()
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.destroyPlayer()
    }
}