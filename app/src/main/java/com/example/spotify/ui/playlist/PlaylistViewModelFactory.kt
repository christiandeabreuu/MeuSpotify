package com.example.spotify.ui.playlist

import GetPlaylistsUseCase
import PlaylistRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spotify.data.local.SpotifyDAO
import com.example.spotify.data.network.SpotifyApiService
import com.example.spotify.domain.usecase.GetUserProfilePlaylistUseCase

class PlaylistViewModelFactory(
    private val apiService: SpotifyApiService,
    private val dao: SpotifyDAO,
    private val accessToken: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaylistViewModel::class.java)) {
            val repository = PlaylistRepository( dao, apiService)
            return PlaylistViewModel(
                getUserProfilePlaylistUseCase = GetUserProfilePlaylistUseCase(apiService),
                getPlaylistsUseCase = GetPlaylistsUseCase(dao,apiService,repository),
                accessToken = accessToken
            ) as T
        }
        throw IllegalArgumentException("Classe ViewModel desconhecida")
    }
}



