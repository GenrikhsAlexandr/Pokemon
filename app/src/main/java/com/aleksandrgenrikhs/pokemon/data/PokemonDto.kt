package com.aleksandrgenrikhs.pokemon.data

import kotlinx.serialization.Serializable

@Serializable
data class PokemonDto (
    val name: String,
    val url: String,
)