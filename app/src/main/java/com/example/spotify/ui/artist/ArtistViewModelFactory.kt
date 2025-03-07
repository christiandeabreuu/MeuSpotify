package com.example.spotify.ui.artist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spotify.auth.SpotifyAuthHelper
import com.example.spotify.data.network.RetrofitInstance
import com.example.spotify.data.repository.AuthRepositoryImpl

class ArtistViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArtistViewModel::class.java)) {
            val authHelper = SpotifyAuthHelper(context)
            val authRepository = AuthRepositoryImpl(context, authHelper)
            val spotifyApiService = RetrofitInstance.api
            return ArtistViewModel(authRepository, spotifyApiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
