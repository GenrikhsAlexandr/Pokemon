package com.aleksandrgenrikhs.pokemon.data

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("pokemon/")
    suspend fun getPokemon(
        @Query(QUERY_PARAM_OFFSET) offset: Int?,
        @Query(QUERY_PARAM_LIMIT) limit: Int,
    ): PokemonListDto

    companion object {
        private const val QUERY_PARAM_OFFSET = "offset"
        private const val QUERY_PARAM_LIMIT = "limit"
    }
}