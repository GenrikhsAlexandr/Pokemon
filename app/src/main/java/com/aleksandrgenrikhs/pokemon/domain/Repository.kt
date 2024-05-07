package com.aleksandrgenrikhs.pokemon.domain

import com.aleksandrgenrikhs.pokemon.ResultState
import kotlinx.coroutines.flow.Flow


interface Repository {

    suspend fun getPokemon(offset: Int?): ResultState<List<Pokemon>?>

    suspend fun getOffset(): Offset

    suspend fun getDetailPokemon(urlDetail: String): ResultState<PokemonDetail>
}