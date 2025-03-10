package com.example.spotify.domain.usecase

import android.util.Log
import com.example.spotify.data.model.Album
import com.example.spotify.data.network.SpotifyApiService
import retrofit2.awaitResponse

class GetArtistAlbumsUseCase(private val spotifyApiService: SpotifyApiService) {
    suspend fun execute(accessToken: String, artistId: String): List<Album>? {
        Log.d("GetArtistAlbumsUseCase", "Executando com ACCESS_TOKEN=$accessToken e ARTIST_ID=$artistId")
        Log.d("AlbumsApi", "Endpoint chamado: /albums?artist_id=$artistId com token=$accessToken")
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
