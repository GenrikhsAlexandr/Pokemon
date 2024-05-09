package com.aleksandrgenrikhs.pokemon.domain

data class Page(
    val nextOffset: Int? =0,
    val nextLimit: Int? =20,
    val previousOffset:Int?,
    val previousLimit:Int?,
    val pokemon: List <Pokemon>,
)
