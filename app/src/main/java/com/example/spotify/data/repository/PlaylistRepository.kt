package com.example.spotify.data.repository

import com.example.spotify.data.local.PlaylistDB
import com.example.spotify.data.local.SpotifyDAO
import com.example.spotify.data.model.Playlist
import com.example.spotify.data.network.SpotifyApiService

class PlaylistRepository(
    private val apiService: SpotifyApiService,
    private val spotifyDAO: SpotifyDAO // Conexão com o banco de dados
) {
    // Busca playlists da API
    suspend fun getPlaylistsFromApi(accessToken: String): List<Playlist>? {
        return try {
            val response = apiService.getPlaylists("Bearer $accessToken")
            response.items
        } catch (e: Exception) {
            null // Retorna null se a API falhar
        }
    }

    // Insere playlists no banco
    suspend fun insertPlaylistsIntoDB(playlists: List<PlaylistDB>) {
        spotifyDAO.insertPlaylists(playlists)
    }

    // Busca playlists do banco de dados
    suspend fun getPlaylistsFromDB(): List<PlaylistDB> {
        return spotifyDAO.getPlaylists()
    }
}
