package com.example.spotify.domain.usecase

import com.example.spotify.data.model.Artist
import com.example.spotify.data.network.SpotifyApiService
import retrofit2.awaitResponse
import android.util.Log


class GetTopArtistsUseCase(private val apiService: SpotifyApiService) {
    suspend fun execute(accessToken: String): List<Artist>? {
        Log.d("GetTopArtistsUseCase", "execute() chamado com accessToken: $accessToken")
        return try {
            val response = apiService.getTopArtists("Bearer $accessToken").awaitResponse()
            if (response.isSuccessful) {
                val items = response.body()?.items
                Log.d("GetTopArtistsUseCase", "Requisição bem-sucedida. Número de artistas: ${items?.size ?: 0}")
                items
            } else {
                Log.e("GetTopArtistsUseCase", "Requisição falhou. Código: ${response.code()}, Mensagem: ${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.e("GetTopArtistsUseCase", "Exceção na requisição: ${e.message}", e)
            null
        }
    }
}

