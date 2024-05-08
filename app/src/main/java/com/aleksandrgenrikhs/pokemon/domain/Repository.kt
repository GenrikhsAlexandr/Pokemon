package com.aleksandrgenrikhs.pokemon.domain

import com.aleksandrgenrikhs.pokemon.ResultState


interface Repository {

    suspend fun getPokemon(offset: Int?): ResultState<Page?>

    suspend fun getDetailPokemon(urlDetail: String): ResultState<PokemonDetail>
}