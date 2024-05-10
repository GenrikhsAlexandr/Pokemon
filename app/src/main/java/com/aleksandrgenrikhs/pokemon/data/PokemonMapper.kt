package com.aleksandrgenrikhs.pokemon.data

import android.net.Uri
import com.aleksandrgenrikhs.pokemon.domain.Page
import com.aleksandrgenrikhs.pokemon.domain.Pokemon
import com.aleksandrgenrikhs.pokemon.domain.PokemonDetail
import com.aleksandrgenrikhs.pokemon.utils.UrlConstants.MAIN_IMAGE_URL
import com.aleksandrgenrikhs.pokemon.utils.UrlConstants.SHOW_DOWN_BACK_IMAGE_URL
import com.aleksandrgenrikhs.pokemon.utils.UrlConstants.SHOW_DOWN_FRONT_IMAGE_URL
import javax.inject.Inject

class PokemonMapper
@Inject constructor() {

    fun mapToPage(page: PokemonListDto): Page {
        val nextOffset = extractOffsetFromUrl(page.next)
        val previousOffset = extractOffsetFromUrl(page.previous)
        return Page(
            count = page.count,
            nextOffset = nextOffset,
            previousOffset = previousOffset,
            pokemon = page.results.map {
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

    private fun extractOffsetFromUrl(url: String?): Int {
        val uri = try {
            Uri.parse(url)
        } catch (e: Exception) {
            return -1
        }
        return uri.getQueryParameter("offset")!!.toInt()
    }

    private fun extractIdFromUrl(url: String): Int {
        val uri = try {
            Uri.parse(url)
        } catch (e: Exception) {
            return 0
        }
        return uri.path?.split("/")?.filter {
            it.isNotEmpty()
        }?.last()?.toInt() ?:0
    }
}