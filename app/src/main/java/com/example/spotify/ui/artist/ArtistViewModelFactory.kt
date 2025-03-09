package com.example.spotify.ui.artist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spotify.data.repository.AuthRepositoryImpl
import com.example.spotify.data.network.RetrofitInstance
import com.example.spotify.domain.usecase.GetAccessTokenUseCase
import com.example.spotify.domain.usecase.GetTopArtistsUseCase
import com.example.spotify.domain.usecase.GetUserProfileUseCase
import com.example.spotify.domain.usecase.LoadTokensUseCase
import com.example.spotify.domain.usecase.RefreshAccessTokenUseCase
import com.example.spotify.domain.usecase.SaveTokensUseCase

class ArtistViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArtistViewModel::class.java)) {
            val authRepository = AuthRepositoryImpl(context)
            val spotifyApiService = RetrofitInstance.api

            val loadTokensUseCase = LoadTokensUseCase(authRepository)
            val saveTokensUseCase = SaveTokensUseCase(authRepository)
            val getUserProfileUseCase = GetUserProfileUseCase(spotifyApiService)
            val refreshAccessTokenUseCase = RefreshAccessTokenUseCase(authRepository)
            val getTopArtistsUseCase = GetTopArtistsUseCase(spotifyApiService)
            val getAccessTokenUseCase = GetAccessTokenUseCase(authRepository)

            return ArtistViewModel(
                loadTokensUseCase,
                saveTokensUseCase,
                getUserProfileUseCase,
                refreshAccessTokenUseCase,
                getTopArtistsUseCase,
                getAccessTokenUseCase

            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


