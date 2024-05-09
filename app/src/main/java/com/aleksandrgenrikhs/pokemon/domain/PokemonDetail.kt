package com.aleksandrgenrikhs.pokemon.domain

data class PokemonDetail (
    val id: Int,
    val name: String,
    val height: String,
    val weight: String,
    val cries: String,
    val experience:String,
    val iconFrontUrl: String,
    val iconBackUrl: String,
)