package com.aleksandrgenrikhs.pokemon.domain

import javax.inject.Inject

class PokemonInteractor
    @Inject constructor(
    private val repository: Repository
) {

    suspend fun getFirstPage(page: Page?) = repository.getFirstPage(page)

    suspend fun getNextPage(page: Page) = repository.getNextPage(page)

    suspend fun getPreviousPage(page: Page) = repository.getPreviousPage(page)

    suspend fun getDetailPokemon(pokemonId:Int) = repository.getDetailPokemon(pokemonId)

    fun isNetWorkConnected() = repository.isNetWorkConnected()
}