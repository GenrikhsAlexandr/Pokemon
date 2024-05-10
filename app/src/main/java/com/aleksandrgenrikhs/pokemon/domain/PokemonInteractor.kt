package com.aleksandrgenrikhs.pokemon.domain

import javax.inject.Inject

class PokemonInteractor
    @Inject constructor(
    private val repository: Repository
) {

    suspend fun getFirstPage() = repository.getFirstPage()

    suspend fun getNextPage(offset:Int) = repository.getNextPage(offset)

    suspend fun getPreviousPage(offset:Int) = repository.getPreviousPage(offset)

    suspend fun getDetailPokemon(pokemonId:Int) = repository.getDetailPokemon(pokemonId)
}