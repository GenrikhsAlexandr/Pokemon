package com.aleksandrgenrikhs.pokemon.di.viewModel

import androidx.lifecycle.ViewModel
import com.aleksandrgenrikhs.pokemon.presentation.viewmodel.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
interface ViewModelsBindingModule {

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(MainViewModel::class)
    fun provideMainViewModel(mainViewModel: MainViewModel): ViewModel
}