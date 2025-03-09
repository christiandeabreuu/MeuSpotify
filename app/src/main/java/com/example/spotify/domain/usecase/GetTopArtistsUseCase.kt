package com.example.spotify.domain.usecase

import com.example.spotify.data.model.Artist
import com.example.spotify.data.network.SpotifyApiService
import retrofit2.awaitResponse
import android.util.Log
import com.example.spotify.data.model.TopArtistsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetTopArtistsUseCase(private val apiService: SpotifyApiService) {
    suspend fun execute(accessToken: String, offset: Int = 0): TopArtistsResponse {
        Log.d(
            "GetTopArtistsUseCase",
            "Chamada API com: accessToken=Bearer $accessToken, offset=$offset"
        )
        return apiService.getTopArtists(
            accessToken = "Bearer $accessToken",
            limit = 20, // ou outro valor que você queira
            timeRange = "medium_term",
            offset = offset
        )
    }
}

