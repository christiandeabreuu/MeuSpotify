package com.example.spotify.ui.playlist

import GetUserPlaylistsUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spotify.data.local.SpotifyDAO
import com.example.spotify.data.network.SpotifyApiService
import com.example.spotify.domain.usecase.GetPlaylistUseCase

class PlaylistViewModelFactory(
    private val apiService: SpotifyApiService,
    private val spotifyDAO: SpotifyDAO,
    private val accessToken: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaylistViewModel::class.java)) {
            val getPlaylistUserProfileUseCase = GetPlaylistUseCase(apiService, spotifyDAO)
            val getUserPlaylistsUseCase = GetUserPlaylistsUseCase(apiService)
            return PlaylistViewModel(getPlaylistUserProfileUseCase, getUserPlaylistsUseCase, accessToken) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

