package com.aleksandrgenrikhs.pokemon.domain

import javax.inject.Inject

class PokemonInteractor
    @Inject constructor(
    private val repository: Repository
) {
    suspend fun getPokemon(offset:Int?, limit:Int?) = repository.getPokemon(offset, limit)

    suspend fun getDetailPokemon(pokemonId:Int) = repository.getDetailPokemon(pokemonId)

    suspend fun getCries(url:String) = repository.initPlayer(url)
    fun playerDestroy() = repository.playerDestroy()
}