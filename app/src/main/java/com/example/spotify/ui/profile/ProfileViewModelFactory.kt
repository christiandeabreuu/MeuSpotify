package com.example.spotify.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spotify.data.local.SpotifyDAO
import com.example.spotify.data.network.SpotifyApiService
import com.example.spotify.data.repository.UserProfileRepository
import com.example.spotify.domain.usecase.GetProfileUserUseCase

class ProfileViewModelFactory(
    private val apiService: SpotifyApiService,
    private val spotifyDAO: SpotifyDAO,
    private val repository: UserProfileRepository,
    private val accessToken: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            val getProfileUserUseCase = GetProfileUserUseCase(spotifyDAO, apiService, repository)
            return ProfileViewModel(getProfileUserUseCase, accessToken) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
