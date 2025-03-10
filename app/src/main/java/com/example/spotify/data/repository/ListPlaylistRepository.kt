
package com.example.spotify.data.repository

import android.util.Log
import com.example.spotify.data.local.PlaylistDB
import com.example.spotify.data.local.SpotifyDAO
import com.example.spotify.data.model.PlaylistsResponse
import com.example.spotify.data.network.SpotifyApiService

class ListPlaylistRepository(private val apiService: SpotifyApiService, private val spotifyDAO: SpotifyDAO) {

    // Busca playlists da API
    suspend fun getPlaylistsApi(accessToken: String): PlaylistsResponse {
        Log.d("PlaylistRepository", "Buscando playlists da API com token: Bearer $accessToken")
        return apiService.getUserPlaylists("Bearer $accessToken")
    }

    // Insere playlists no banco de dados
    suspend fun insertPlaylists(playlists: List<PlaylistDB>) {
        spotifyDAO.insertPlaylists(playlists)
        Log.d("PlaylistRepository", "Playlists inseridas no banco: $playlists")
    }

    // Busca playlists do banco de dados
    suspend fun getPlaylistsDB(): List<PlaylistDB> {
        val playlists = spotifyDAO.getPlaylists()
        Log.d("PlaylistRepository", "Playlists recuperadas do banco: $playlists")
        return playlists
    }
}
