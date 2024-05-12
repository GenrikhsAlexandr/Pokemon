package com.aleksandrgenrikhs.pokemon.domain

import com.aleksandrgenrikhs.pokemon.data.PokemonRepositoryImpl.Companion.LIMIT

data class Page(
    val nextOffset: Int = 0,
    val previousOffset:Int,
    val pokemon: List <Pokemon>,
    val count: Int
)
{
    val hasNextPage: Boolean
        get() = nextOffset >= 0

    val hasPreviousPage: Boolean
        get() = previousOffset >= 0

    val pageNumberNext: Int
        get() = nextOffset / LIMIT

    val pageNumberPrevious: Int
        get() = previousOffset / LIMIT

}