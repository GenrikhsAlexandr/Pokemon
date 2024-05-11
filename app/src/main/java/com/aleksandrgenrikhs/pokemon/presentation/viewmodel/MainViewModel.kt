package com.aleksandrgenrikhs.pokemon.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aleksandrgenrikhs.pokemon.R
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
    private val interactor: PokemonInteractor,
) : ViewModel() {

    private val _page: MutableStateFlow<Page?> = MutableStateFlow(null)
    val page: StateFlow<Page?> = _page

    private val _isReady: MutableStateFlow<Boolean> =  MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady

    private val _isProgressBarVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isProgressBarVisible: StateFlow<Boolean> = _isProgressBarVisible

    private val _isErrorLayoutVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isErrorLayoutVisible: StateFlow<Boolean> = _isErrorLayoutVisible

    private val _isButtonGroupVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isButtonGroupVisible: StateFlow<Boolean> = _isButtonGroupVisible

    val toastMessageError: MutableSharedFlow<Int> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    fun getFirstPage() {
        viewModelScope.launch {
            _isErrorLayoutVisible.value = false
            when (val result = interactor.getFirstPage()) {
                is ResultState.Error -> {
                    _isErrorLayoutVisible.value = true
                    _isReady.value = true
                    if (!interactor.isNetWorkConnected()) {
                        toastMessageError.tryEmit(R.string.error_message)
                    } else {
                        toastMessageError.tryEmit(result.message)
                    }
                }

                is ResultState.Success -> {
                    _isErrorLayoutVisible.value = false
                    _page.value = result.data
                    _isReady.value = true
                }
            }
        }
    }

    fun getNextPage() {
        viewModelScope.launch {
            _isProgressBarVisible.value = true
            when (val result = interactor.getNextPage(page.value!!.nextOffset)) {
                is ResultState.Error -> {
                    if (!interactor.isNetWorkConnected()) {
                        toastMessageError.tryEmit(R.string.error_message)
                    } else {
                        toastMessageError.tryEmit(result.message)
                    }
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
            when (val result = interactor.getPreviousPage(page.value!!.previousOffset)) {
                is ResultState.Error -> {
                    if (!interactor.isNetWorkConnected()) {
                        toastMessageError.tryEmit(R.string.error_message)
                    } else {
                        toastMessageError.tryEmit(result.message)
                    }
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