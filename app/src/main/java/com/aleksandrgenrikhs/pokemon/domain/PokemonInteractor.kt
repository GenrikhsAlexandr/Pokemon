package com.aleksandrgenrikhs.pokemon.domain

import javax.inject.Inject

class PokemonInteractor
    @Inject constructor(
    private val repository: Repository
) {
    suspend fun getPokemon(offset:Int?) = repository.getPokemon(offset)

    suspend fun getDetailPokemon(pokemonId:Int) = repository.getDetailPokemon(pokemonId)

    suspend fun getCries(url:String) = repository.initPlayer(url)
    fun playerDestroy() = repository.playerDestroy()
}