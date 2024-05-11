package com.aleksandrgenrikhs.pokemon.data

import android.app.Application
import com.aleksandrgenrikhs.pokemon.R
import com.aleksandrgenrikhs.pokemon.domain.NetworkConnectionChecker
import com.aleksandrgenrikhs.pokemon.domain.Page
import com.aleksandrgenrikhs.pokemon.domain.PokemonDetail
import com.aleksandrgenrikhs.pokemon.domain.Repository
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
) : Repository {

    companion object {

        private const val LIMIT = 20
    }

    override suspend fun getFirstPage(): ResultState<Page> {
        return withContext(Dispatchers.IO) {
            if (!networkConnected.isNetworkConnected(application)) {
                return@withContext ResultState.Error(R.string.error_message)
            } else {
                try {
                    val response = service.getFirstPage()
                    return@withContext ResultState.Success(mapper.mapToPage(response))
                } catch (e: Exception) {
                    e.printStackTrace()
                    return@withContext ResultState.Error(R.string.error_message_response)
                }
            }
        }
    }

    override suspend fun getNextPage(offset: Int): ResultState<Page> {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.getPage(offset = offset, limit = LIMIT)
                return@withContext ResultState.Success(mapper.mapToPage(response))
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext ResultState.Error(R.string.error_message_response)
            }
        }
    }

    override suspend fun getPreviousPage(offset: Int): ResultState<Page> {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.getPage(offset = offset, limit = LIMIT)
                return@withContext ResultState.Success(mapper.mapToPage(response))
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext ResultState.Error(R.string.error_message_response)
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

    override fun isNetWorkConnected():Boolean {
        return networkConnected.isNetworkConnected(application)
    }
}