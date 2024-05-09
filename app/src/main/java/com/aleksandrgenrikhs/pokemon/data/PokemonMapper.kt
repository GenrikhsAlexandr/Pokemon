package com.aleksandrgenrikhs.pokemon.data

import com.aleksandrgenrikhs.pokemon.domain.Page
import com.aleksandrgenrikhs.pokemon.domain.Pokemon
import com.aleksandrgenrikhs.pokemon.domain.PokemonDetail
import com.aleksandrgenrikhs.pokemon.utils.UrlConstants.MAIN_IMAGE_URL
import com.aleksandrgenrikhs.pokemon.utils.UrlConstants.SHOW_DOWN_BACK_IMAGE_URL
import com.aleksandrgenrikhs.pokemon.utils.UrlConstants.SHOW_DOWN_FRONT_IMAGE_URL
import javax.inject.Inject

class PokemonMapper
@Inject constructor() {

    fun mapToPokemon(offset: PokemonListDto): Page {
        val nextOffset = extractOffsetFromUrl(offset.next)
        val previousOffset = extractOffsetFromUrl(offset.previous)
        return Page(
            next = nextOffset,
            previous = previousOffset,
            pokemon = offset.results.map {
                val pokemonId = extractIdFromUrl(it.url)
                Pokemon(
                    id = pokemonId,
                    name = it.name,
                    iconUrl = "$MAIN_IMAGE_URL$pokemonId.png"
                )
            }
        )
    }

    fun mapTODetail(pokemon: PokemonDetailDto): PokemonDetail {
        with(pokemon) {
            return PokemonDetail(
                id = id,
                name = name,
                height = height.toString(),
                weight = weight.toString(),
                cries = cries.latest,
                experience = base_experience.toString(),
                iconBackUrl = "$SHOW_DOWN_BACK_IMAGE_URL$id.gif",
                iconFrontUrl = "$SHOW_DOWN_FRONT_IMAGE_URL$id.gif"
            )
        }
    }

    private fun extractOffsetFromUrl(url: String?): Int? {
        if (url == null) {
            return null
        }
        val regex = Regex("""\?offset=(\d+)""")
        val matchResult = regex.find(url)
        return matchResult?.groupValues?.get(1)?.toInt()
    }

    private fun extractIdFromUrl(url: String): Int {
        val regex = Regex("""/(\d+)/""")
        val matchResult = regex.find(url)
        return matchResult?.groupValues?.get(1)?.toInt() ?: -1
    }
}