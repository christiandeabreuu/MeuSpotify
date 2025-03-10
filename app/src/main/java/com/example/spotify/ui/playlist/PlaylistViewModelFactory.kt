package com.example.spotify.ui.playlist

import GetPlaylistsUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spotify.data.local.SpotifyDAO
import com.example.spotify.data.network.SpotifyApiService
import com.example.spotify.data.repository.PlaylistRepository
import com.example.spotify.domain.usecase.GetUserProfilePlaylistUseCase

class PlaylistViewModelFactory(
    private val apiService: SpotifyApiService,
    private val spotifyDAO: SpotifyDAO, // Adicionar o DAO como um parâmetro
    private val accessToken: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaylistViewModel::class.java)) {
            return PlaylistViewModel(
                getUserProfilePlaylistUseCase = GetUserProfilePlaylistUseCase(apiService),
                getPlaylistsUseCase = GetPlaylistsUseCase(PlaylistRepository(apiService, spotifyDAO)),
                accessToken = accessToken
            ) as T
        }
        throw IllegalArgumentException("Classe ViewModel desconhecida")
    }
}


