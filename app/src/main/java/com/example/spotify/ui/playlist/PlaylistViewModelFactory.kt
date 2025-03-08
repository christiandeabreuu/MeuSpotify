package com.example.spotify.ui.playlist

import GetUserPlaylistsUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spotify.data.network.SpotifyApiService
import com.example.spotify.domain.usecase.GetPlaylistUserProfileUseCase

class PlaylistViewModelFactory(
    private val apiService: SpotifyApiService,
    private val accessToken: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaylistViewModel::class.java)) {
            val getPlaylistUserProfileUseCase = GetPlaylistUserProfileUseCase(apiService)
            val getUserPlaylistsUseCase = GetUserPlaylistsUseCase(apiService)
            return PlaylistViewModel(getPlaylistUserProfileUseCase, getUserPlaylistsUseCase, accessToken) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

