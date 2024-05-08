package com.aleksandrgenrikhs.pokemon.data

import com.aleksandrgenrikhs.pokemon.domain.Page
import com.aleksandrgenrikhs.pokemon.domain.Pokemon
import javax.inject.Inject

class PokemonMapper
@Inject constructor(){

    fun mapToPokemon(offset: PokemonListDto):  Page {
        val nextOffset = extractOffsetFromUrl(offset.next)
        val previousOffset = extractOffsetFromUrl(offset.previous)
        return Page (
            next = nextOffset,
            previous = previousOffset,
            pokemon = offset.results.map {
                val pokemonId = extractIdFromUrl(it.url)
                Pokemon(
                  id = pokemonId,
                    name = it.name,
                    url = it.url,
              )
            }
        )
    }

    private fun extractOffsetFromUrl(url: String?): Int? {
        if (url == null) {
            return null
        }
        val regex = Regex("""\?offset=(\d+)""")
        val matchResult = regex.find(url)
        return matchResult?.groupValues?.get(1)?.toInt()
    }

    private fun extractIdFromUrl(url:String): Int {
        val regex = Regex("""/(\d+)/""")
        val matchResult = regex.find(url)
        return matchResult?.groupValues?.get(1)?.toInt() ?: -1
    }
}