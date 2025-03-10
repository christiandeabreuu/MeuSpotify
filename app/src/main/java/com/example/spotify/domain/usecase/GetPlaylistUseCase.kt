package com.example.spotify.domain.usecase

import com.example.spotify.data.local.SpotifyDAO
import com.example.spotify.data.model.Playlist
import com.example.spotify.data.network.SpotifyApiService
import com.example.spotify.data.repository.ListPlaylistRepository

import android.util.Log
import com.example.spotify.data.local.PlaylistDB
import com.example.spotify.data.model.PlaylistsResponse

class GetPlaylistUseCase(
    private val apiService: SpotifyApiService,
    private val spotifyDAO: SpotifyDAO,
    private val repositoryList: ListPlaylistRepository = ListPlaylistRepository(apiService, spotifyDAO)
) {
    // Busca playlists da API e salva no banco
    suspend fun getPlaylistsFromApi(accessToken: String): PlaylistsResponse {
        Log.d("GetPlaylistsUseCase", "Buscando playlists da API com token: Bearer $accessToken")
        val responseApi = repositoryList.getPlaylistsApi(accessToken)

        // Mapeando e salvando no banco de dados
        val playlistsDB = responseApi.items.map { playlist ->
            playlist.toPlaylistDB()
        }
        repositoryList.insertPlaylists(playlistsDB)

        return responseApi
    }

    // Busca playlists do banco de dados
    suspend fun getPlaylistsFromDB(): List<PlaylistDB> {
        Log.d("GetPlaylistsUseCase", "Buscando playlists do banco de dados")
        return repositoryList.getPlaylistsDB()
    }

    fun Playlist.toPlaylistDB(): PlaylistDB {
        return PlaylistDB(
            id = this.id,
            name = this.name,
            description = this.description,
            ownerName = this.owner.name,
            tracksCount = this.tracksCount,
            imageUrl = this.images.firstOrNull()?.url
        )
    }
}

