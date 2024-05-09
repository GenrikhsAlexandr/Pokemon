package com.aleksandrgenrikhs.pokemon.data

import android.app.Application
import android.media.MediaPlayer
import android.net.Uri
import com.aleksandrgenrikhs.pokemon.R
import com.aleksandrgenrikhs.pokemon.domain.NetworkConnectionChecker
import com.aleksandrgenrikhs.pokemon.domain.Page
import com.aleksandrgenrikhs.pokemon.domain.PokemonDetail
import com.aleksandrgenrikhs.pokemon.domain.Repository
import com.aleksandrgenrikhs.pokemon.utils.ResultState
import com.aleksandrgenrikhs.pokemon.utils.UrlConstants.BASE_URL
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.IOException
import javax.inject.Inject

class RepositoryImpl
@Inject constructor(
    private val mapper: PokemonMapper,
    private val networkConnected: NetworkConnectionChecker,
    private val application: Application
) : Repository {

    private var mediaPlayer: MediaPlayer? = null

    companion object {
        const val LIMIT = 20
        private val json = Json { ignoreUnknownKeys = true }
    }

    @OptIn(ExperimentalSerializationApi::class)
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
                    return@withContext ResultState.Success(mapper.mapToPokemon(response))
                } catch (e: Exception) {
                    e.printStackTrace()
                    return@withContext ResultState.Error(R.string.error_message_response)
                }
            }
        }
    }

    override suspend fun getDetailPokemon(pokemonId: Int): ResultState<PokemonDetail> =
        withContext(Dispatchers.IO) {
            if (!networkConnected.isNetworkConnected(application)) {
                return@withContext ResultState.Error(R.string.error_message)
            } else {
                try {
                    val response = service.getDetailPokemon(id = pokemonId)
                    return@withContext ResultState.Success(mapper.mapTODetail(response))
                } catch (e: Exception) {
                    e.printStackTrace()
                    return@withContext ResultState.Error(R.string.error_message_response)
                }
            }
        }

    override suspend fun initPlayer(url: String): ResultState<MediaPlayer?> =
        withContext(Dispatchers.IO) {
            try {
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(application, Uri.parse(url))
                    prepare()
                   start()
                }
                ResultState.Success(mediaPlayer)
            } catch (e: IOException) {
                e.printStackTrace()
                ResultState.Error(R.string.error_message_response)
            }
        }

    override fun playerDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}