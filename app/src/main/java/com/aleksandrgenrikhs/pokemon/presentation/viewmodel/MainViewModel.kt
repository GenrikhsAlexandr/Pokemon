package com.aleksandrgenrikhs.pokemon.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aleksandrgenrikhs.pokemon.domain.Page
import com.aleksandrgenrikhs.pokemon.domain.PokemonInteractor
import com.aleksandrgenrikhs.pokemon.utils.ResultState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel
@Inject constructor(
    private val interator: PokemonInteractor,
) : ViewModel() {

    private val _page: MutableStateFlow<Page?> = MutableStateFlow(null)
    val page: StateFlow<Page?> = _page

    val isReady: StateFlow<Boolean> = page.map {
        it != null
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val _isProgressBarVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isProgressBarVisible: StateFlow<Boolean> = _isProgressBarVisible

    private val _isButtonGroupVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isButtonGroupVisible: StateFlow<Boolean> = _isButtonGroupVisible

    val toastMessageError: MutableSharedFlow<ResultState.Error> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    fun getFirstPage() {
        viewModelScope.launch {
            when (val result = interator.getFirstPage()) {
                is ResultState.Error -> {
                    toastMessageError.tryEmit(ResultState.Error(result.message))
                }

                is ResultState.Success -> {
                    _page.value = result.data
                }
            }
        }
    }

    fun getNextPage() {
        viewModelScope.launch {
            _isProgressBarVisible.value = true
            when (val result = interator.getNextPage(page.value!!.nextOffset)) {
                is ResultState.Error -> {
                    toastMessageError.tryEmit(ResultState.Error(result.message))
                }

                is ResultState.Success -> {
                    _page.value = result.data
                }
            }
            _isProgressBarVisible.value = false
        }
    }

    fun getPreviousPage() {
        viewModelScope.launch {
            _isProgressBarVisible.value = true
            when (val result = interator.getPreviousPage(page.value!!.previousOffset)) {
                is ResultState.Error -> {
                    toastMessageError.tryEmit(ResultState.Error(result.message))
                }

                is ResultState.Success -> {
                    _page.value = result.data
                }
            }
            _isProgressBarVisible.value = false
        }
    }

    fun showButtonGroup() {
        _isButtonGroupVisible.value = true
    }

    fun hideButtonGroup() {
        _isButtonGroupVisible.value = false
    }
}