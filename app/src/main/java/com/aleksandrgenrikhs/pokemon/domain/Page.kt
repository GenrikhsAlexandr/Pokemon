package com.aleksandrgenrikhs.pokemon.domain

data class Page(
    val next:Int? = 0,
    val previous:Int? = null,
    val pokemon: List <Pokemon>
)
