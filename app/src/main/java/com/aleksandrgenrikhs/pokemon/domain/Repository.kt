package com.aleksandrgenrikhs.pokemon.domain

import android.media.MediaPlayer
import com.aleksandrgenrikhs.pokemon.ResultState


interface Repository {

    suspend fun getPokemon(offset: Int?): ResultState<Page?>

    suspend fun getDetailPokemon(pokemonId: Int): ResultState<PokemonDetail>

    suspend fun initPlayer(url: String): ResultState<MediaPlayer?>

    fun playerDestroy()
}