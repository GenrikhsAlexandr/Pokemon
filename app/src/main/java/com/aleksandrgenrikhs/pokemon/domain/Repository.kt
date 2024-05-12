package com.aleksandrgenrikhs.pokemon.domain

import com.aleksandrgenrikhs.pokemon.utils.ResultState

interface Repository {

    suspend fun getFirstPage(): ResultState<Page>

    suspend fun getNextPage(page: Page): ResultState<Page>

    suspend fun getPreviousPage(page: Page): ResultState<Page>

    suspend fun getDetailPokemon(pokemonId: Int): ResultState<PokemonDetail>

    fun isNetWorkConnected():Boolean
}