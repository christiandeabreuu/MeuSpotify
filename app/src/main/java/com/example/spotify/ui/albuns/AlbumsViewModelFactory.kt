package com.example.spotify.ui.albuns

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spotify.data.SpotifyApiService

class AlbumsViewModelFactory(private val spotifyApiService: SpotifyApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlbumsViewModel::class.java)) {
            return AlbumsViewModel(spotifyApiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
