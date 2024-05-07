package com.aleksandrgenrikhs.pokemon.data

import android.graphics.Bitmap
import com.aleksandrgenrikhs.pokemon.domain.Offset
import com.aleksandrgenrikhs.pokemon.domain.Pokemon
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PokemonMapper
@Inject constructor(){

   suspend fun mapToPokemon(pokemon: PokemonListDto): List <Pokemon> {
        return pokemon.results.map {
            Offset(
                next = pokemon.next?.let { next -> extractIdFromUrl(next) },
                previous = pokemon.previous?.let {previous -> extractIdFromUrl(previous)  }
            )
            val pokemonId = extractIdFromUrl(it.url)
            Pokemon(
                id = pokemonId,
                name = it.name,
                image = setIcon(pokemonId),
                url = it.url,
            )
        }
    }

    fun mapOffsetToUrl(offset: PokemonListDto):  Offset {
        val nextOffset = extractOffsetFromUrl(offset.next)
        val previousOffset = extractOffsetFromUrl(offset.previous)
        return Offset (
            next = nextOffset,
            previous = previousOffset
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

    private suspend fun setIcon(pokemonId: Int): Bitmap? = withContext(Dispatchers.IO) {
        try {
            Picasso.get()
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokemonId.png")
                .get()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}