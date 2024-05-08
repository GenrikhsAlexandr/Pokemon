package com.aleksandrgenrikhs.pokemon.data

import android.app.Application
import com.aleksandrgenrikhs.pokemon.R
import com.aleksandrgenrikhs.pokemon.ResultState
import com.aleksandrgenrikhs.pokemon.domain.NetworkConnectionChecker
import com.aleksandrgenrikhs.pokemon.domain.Page
import com.aleksandrgenrikhs.pokemon.domain.PokemonDetail
import com.aleksandrgenrikhs.pokemon.domain.Repository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Inject

class RepositoryImpl
@Inject constructor(
    private val mapper: PokemonMapper,
    private val networkConnected: NetworkConnectionChecker,
    private val application: Application
) : Repository {

    companion object {
        const val LIMIT = 20
        private const val BASE_URL = "https://pokeapi.co/api/v2/"
        const val IMAGE_URL =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
        private val json = Json { ignoreUnknownKeys = true }
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .client(
            OkHttpClient.Builder()
                .apply {
                    addInterceptor(
                        HttpLoggingInterceptor().setLevel(
                            HttpLoggingInterceptor
                                .Level.BODY
                        )
                    )
                }
                .build()
        )
        .build()

    private val service: ApiService = retrofit.create(ApiService::class.java)

    override suspend fun getPokemon(offset: Int?): ResultState<Page?> {
        return withContext(Dispatchers.IO) {
            if (!networkConnected.isNetworkConnected(application)) {
                return@withContext ResultState.Error(R.string.error_message)
            } else {
                try {
                    val response = service.getPokemon(offset = offset, limit = LIMIT)
                    val pokemonList = mapper.mapToPokemon(response)
                    return@withContext ResultState.Success(pokemonList)
                } catch (e: Exception) {
                    e.printStackTrace()
                    return@withContext ResultState.Error(R.string.error_message_response)
                }
            }
        }
    }

    //  override suspend fun getOffset(): Offset = mapper.mapOffsetToUrl(response)

    override suspend fun getDetailPokemon(urlDetail: String): ResultState<PokemonDetail> {
        TODO("Not yet implemented")
    }
}