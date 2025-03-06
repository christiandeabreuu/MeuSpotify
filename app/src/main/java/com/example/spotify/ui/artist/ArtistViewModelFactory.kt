package com.example.spotify.ui.artist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spotify.auth.SpotifyAuthHelper

class ArtistViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArtistViewModel::class.java)) {
            return ArtistViewModel(SpotifyAuthHelper(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
