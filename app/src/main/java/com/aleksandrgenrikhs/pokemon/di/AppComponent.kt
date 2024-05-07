package com.aleksandrgenrikhs.pokemon.di

import android.app.Application
import com.aleksandrgenrikhs.pokemon.di.viewModel.ViewModelFactory
import com.aleksandrgenrikhs.pokemon.presentation.MainFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        AppModule::class,
    ]
)

@Singleton
interface AppComponent {
    companion object {

        fun init(application: Application): AppComponent {
            return DaggerAppComponent.factory().create(application)
        }
    }

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application,
        ): AppComponent
    }

    val viewModelFactory: ViewModelFactory

    fun inject(mainFragment: MainFragment)
}