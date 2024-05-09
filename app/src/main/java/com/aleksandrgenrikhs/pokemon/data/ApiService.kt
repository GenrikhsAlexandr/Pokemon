package com.aleksandrgenrikhs.pokemon.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("pokemon/")
    suspend fun getPokemon(
        @Query(QUERY_PARAM_OFFSET) offset: Int?,
        @Query(QUERY_PARAM_LIMIT) limit: Int?,
    ): PokemonListDto

    @GET("pokemon/{id}")
    suspend fun getDetailPokemon(
        @Path(QUERY_PARAM_ID) id: Int,
    ): PokemonDetailDto

    companion object {
        private const val QUERY_PARAM_OFFSET = "offset"
        private const val QUERY_PARAM_LIMIT = "limit"
        private const val QUERY_PARAM_ID = "id"
    }
}