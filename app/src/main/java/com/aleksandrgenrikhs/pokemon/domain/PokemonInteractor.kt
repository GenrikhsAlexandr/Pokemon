package com.aleksandrgenrikhs.pokemon.domain

import javax.inject.Inject

class PokemonInteractor
    @Inject constructor(
    private val repository: Repository
) {
    suspend fun getPokemon(offset:Int?) = repository.getPokemon(offset)

    suspend fun getOffset() = repository.getOffset()

    suspend fun getDetailPokemon(url:String) = repository.getDetailPokemon(url)
}