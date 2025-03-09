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
     fun execute(accessToken: String, offset: Int = 0): Flow<TopArtistsResponse> {
        Log.d("GetTopArtistsUseCase", "execute() chamado com accessToken: $accessToken")
        return flow {
            val response = apiService.getTopArtists("Bearer $accessToken", offset = offset)

            emit(requireNotNull( response.awaitResponse().body()))
        }.catch { e ->
            Log.e("GetTopArtistsUseCase", "Exceção na requisição: ${e.message}", e)
        }
    }
}

