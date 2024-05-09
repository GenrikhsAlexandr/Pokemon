package com.aleksandrgenrikhs.pokemon.data

import kotlinx.serialization.Serializable

@Serializable
data class PokemonDetailDto(
    val base_experience: Int,
    val cries: CriesDto,
    val height: Int,
    val id: Int,
    val name: String,
    val weight: Int,
)