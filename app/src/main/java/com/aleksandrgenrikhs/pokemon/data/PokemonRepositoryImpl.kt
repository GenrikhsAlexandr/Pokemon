package com.aleksandrgenrikhs.pokemon.data

import android.app.Application
import com.aleksandrgenrikhs.pokemon.R
import com.aleksandrgenrikhs.pokemon.domain.NetworkConnectionChecker
import com.aleksandrgenrikhs.pokemon.domain.Page
import com.aleksandrgenrikhs.pokemon.domain.PokemonDetail
import com.aleksandrgenrikhs.pokemon.domain.Repository
import com.aleksandrgenrikhs.pokemon.utils.PageCache
import com.aleksandrgenrikhs.pokemon.utils.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PokemonRepositoryImpl
@Inject constructor(
    private val mapper: PokemonMapper,
    private val networkConnected: NetworkConnectionChecker,
    private val application: Application,
    private val service: ApiService,
    private val cache: PageCache
) : Repository {

    companion object {

        const val LIMIT = 20
    }

    override suspend fun getFirstPage(): ResultState<Page> {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.getFirstPage()
                val pageCache = mapper.mapToPage(response)
                val key = 0
                cache.putPage(key, pageCache)
                return@withContext ResultState.Success(mapper.mapToPage(response))
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext ResultState.Error(R.string.error_message_response)
            }
        }
    }

    override suspend fun getNextPage(page: Page): ResultState<Page> {
        return withContext(Dispatchers.IO) {
            val key = page.pageNumberNext
            val cachedPage = cache.getPage(key)
            if (cachedPage != null) {
                return@withContext ResultState.Success(cachedPage)
            } else {
                try {
                    val response = service.getPage(offset = page.nextOffset, limit = LIMIT)
                    val pageCache = mapper.mapToPage(response)
                    cache.putPage(key, pageCache)
                    return@withContext ResultState.Success(pageCache)
                } catch (e: Exception) {
                    e.printStackTrace()
                    return@withContext ResultState.Error(R.string.error_message_response)
                }
            }
        }
    }

    override suspend fun getPreviousPage(page: Page): ResultState<Page> {
        return withContext(Dispatchers.IO) {
            val key = page.pageNumberPrevious
            println("")
            val cachedPage = cache.getPage(key)
            if (cachedPage != null) {
                return@withContext ResultState.Success(cachedPage)
            } else {
                try {
                    val response = service.getPage(offset = page.previousOffset, limit = LIMIT)
                    return@withContext ResultState.Success(mapper.mapToPage(response))
                } catch (e: Exception) {
                    e.printStackTrace()
                    return@withContext ResultState.Error(R.string.error_message_response)
                }
            }
        }
    }

    override suspend fun getDetailPokemon(pokemonId: Int): ResultState<PokemonDetail> =
        withContext(Dispatchers.IO) {
            try {
                val response = service.getDetailPokemon(id = pokemonId)
                return@withContext ResultState.Success(mapper.mapTODetail(response))
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext ResultState.Error(R.string.error_message_response)
            }
        }

    override fun isNetWorkConnected(): Boolean {
        return networkConnected.isNetworkConnected(application)
    }
}