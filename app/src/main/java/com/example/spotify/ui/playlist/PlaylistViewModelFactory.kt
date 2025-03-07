package com.example.spotify.ui.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PlaylistViewModelFactory(private val accessToken: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaylistViewModel::class.java)) {
            return PlaylistViewModel(accessToken) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
