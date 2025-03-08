package com.example.spotify.domain.usecase

import com.example.spotify.data.model.Album
import com.example.spotify.data.network.SpotifyApiService
import retrofit2.awaitResponse

class GetArtistAlbumsUseCase(private val spotifyApiService: SpotifyApiService) {
    suspend fun execute(accessToken: String, artistId: String): List<Album>? {
        return try {
            val response = spotifyApiService.getArtistAlbums("Bearer $accessToken", artistId).awaitResponse()
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
