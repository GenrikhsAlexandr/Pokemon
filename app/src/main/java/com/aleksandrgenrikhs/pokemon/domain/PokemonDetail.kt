package com.aleksandrgenrikhs.pokemon.domain

data class PokemonDetail (

    val id: Int,
    val name: String,
    val imageUrl: String?,
    val types: List<String>,
    val abilities: List<String>,
)