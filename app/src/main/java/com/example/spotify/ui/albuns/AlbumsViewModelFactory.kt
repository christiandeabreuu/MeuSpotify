package com.example.spotify.ui.albuns

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spotify.data.local.SpotifyDAO
import com.example.spotify.data.network.SpotifyApiService
import com.example.spotify.domain.usecase.GetArtistAlbumsUseCase

class AlbumsViewModelFactory(private val spotifyApiService: SpotifyApiService, private val spotifyDao: SpotifyDAO) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlbumsViewModel::class.java)) {
            val getArtistAlbumsUseCase = GetArtistAlbumsUseCase(spotifyApiService)
            return AlbumsViewModel(getArtistAlbumsUseCase, spotifyDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

