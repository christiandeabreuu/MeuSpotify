package com.example.spotify.ui.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.spotify.domain.usecase.*
import kotlinx.coroutines.Dispatchers

class ArtistViewModel(
    private val loadTokensUseCase: LoadTokensUseCase,
    private val saveTokensUseCase: SaveTokensUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val refreshAccessTokenUseCase: RefreshAccessTokenUseCase,
    private val getTopArtistsUseCase: GetTopArtistsUseCase
) : ViewModel() {

    // Carregar tokens
    fun loadTokens() = liveData(Dispatchers.IO) {
        val tokens = loadTokensUseCase.execute()
        emit(tokens)
    }

    // Salvar tokens
    fun saveAccessToken(accessToken: String, refreshToken: String) {
        saveTokensUseCase.execute(accessToken, refreshToken)
    }

    // Obter perfil do usuário
    fun getUserProfile(accessToken: String) = liveData(Dispatchers.IO) {
        val userProfile = getUserProfileUseCase.execute(accessToken)
        emit(userProfile)
    }

    // Renovar token
    fun refreshToken(refreshToken: String) = liveData(Dispatchers.IO) {
        val tokens = refreshAccessTokenUseCase.execute(refreshToken)
        emit(tokens)
    }

    // Buscar principais artistas
    fun getTopArtists(accessToken: String) = liveData(Dispatchers.IO) {
        val topArtists = getTopArtistsUseCase.execute(accessToken)
        emit(topArtists)
    }
}
