package com.example.spotify.domain.usecase

import com.example.spotify.data.model.Artist
import com.example.spotify.data.network.SpotifyApiService
import retrofit2.awaitResponse

class GetTopArtistsUseCase(private val apiService: SpotifyApiService) {
    suspend fun execute(accessToken: String): List<Artist>? {
        return try {
            val response = apiService.getTopArtists("Bearer $accessToken").awaitResponse()
            if (response.isSuccessful) {
                response.body()?.items
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
