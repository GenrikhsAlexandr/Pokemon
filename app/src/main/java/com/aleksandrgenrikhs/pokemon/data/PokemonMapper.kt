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

    fun mapToPokemon(pokemon: PokemonListDto): Page {
        val (nextOffset, nextLimit) = extractOffsetAndLimitFromUrl(pokemon.next)
        val (previousOffset, previousLimit) = extractOffsetAndLimitFromUrl(pokemon.previous)
        return Page(
            nextOffset = nextOffset,
            nextLimit = nextLimit,
            previousOffset = previousOffset,
            previousLimit = previousLimit,
            pokemon = pokemon.results.map {
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

    private fun extractOffsetAndLimitFromUrl(url: String?): Pair<Int?, Int?> {
        if (url == null) {
            return Pair(null, null)
        }
        val matchResult = Regex("""\?offset=(\d+)&limit=(\d+)""").find(url)
        val offset = matchResult?.groupValues?.get(1)?.toInt()
        val limit = matchResult?.groupValues?.get(2)?.toInt()
        return Pair(offset, limit)
    }

    private fun extractIdFromUrl(url: String): Int {
        return Regex("""/(\d+)/""").find(url)?.groupValues?.get(1)?.toInt() ?: 0
    }
}