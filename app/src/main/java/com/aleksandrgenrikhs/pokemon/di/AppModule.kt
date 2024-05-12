package com.aleksandrgenrikhs.pokemon.di

import com.aleksandrgenrikhs.pokemon.data.PokemonRepositoryImpl
import com.aleksandrgenrikhs.pokemon.di.viewModel.ViewModelModule
import com.aleksandrgenrikhs.pokemon.domain.NetworkConnectionChecker
import com.aleksandrgenrikhs.pokemon.domain.Repository
import com.aleksandrgenrikhs.pokemon.utils.NetworkConnected
import com.aleksandrgenrikhs.pokemon.utils.PageCache
import com.aleksandrgenrikhs.pokemon.utils.PokemonMediaPlayer
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        ViewModelModule::class,
        NetworkModule::class,
    ]
)
interface AppModule {

    @Binds
    @Singleton
    fun bindRepository(impl: PokemonRepositoryImpl): Repository

    companion object {
        @Provides
        @Singleton
        fun provideNetworkConnected(): NetworkConnectionChecker = NetworkConnected

        @Provides
        @Singleton
        fun provideMediaPlayer():PokemonMediaPlayer = PokemonMediaPlayer

        @Provides
        @Singleton
        fun providePageCache():PageCache = PageCache
    }
}