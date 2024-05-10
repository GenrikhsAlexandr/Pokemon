package com.aleksandrgenrikhs.pokemon.domain

data class Page(
    val nextOffset: Int,
    val previousOffset:Int,
    val pokemon: List <Pokemon>,
    val count: Int
)
{
    val hasNextPage: Boolean
        get() = nextOffset >= 0

    val hasPreviousPage: Boolean
        get() = previousOffset >= 0
}