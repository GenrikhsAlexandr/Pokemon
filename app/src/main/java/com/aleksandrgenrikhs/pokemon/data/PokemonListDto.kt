package com.aleksandrgenrikhs.pokemon.data

import kotlinx.serialization.Serializable

@Serializable
data class PokemonListDto(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonDto>,
)