package com.aleksandrgenrikhs.pokemon.di

import com.aleksandrgenrikhs.pokemon.NetworkConnected
import com.aleksandrgenrikhs.pokemon.di.viewModel.ViewModelsBindingModule
import com.aleksandrgenrikhs.pokemon.data.RepositoryImpl
import com.aleksandrgenrikhs.pokemon.domain.NetworkConnectionChecker
import com.aleksandrgenrikhs.pokemon.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        ViewModelsBindingModule::class,
    ]
)
interface AppModule {

    @Binds
    @Singleton
    fun bindRepository(impl: RepositoryImpl): Repository

    companion object {
        @Provides
        @Singleton
        fun provideNetworkConnected(): NetworkConnectionChecker = NetworkConnected
    }
}