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
        try {
            val tokens = loadTokensUseCase.execute()
            emit(Result.success(tokens))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    // Salvar tokens
    fun saveAccessToken(accessToken: String, refreshToken: String) {
        saveTokensUseCase.execute(accessToken, refreshToken)
    }

    // Obter perfil do usuário
    fun getUserProfile(accessToken: String) = liveData(Dispatchers.IO) {
        try {
            val userProfile = getUserProfileUseCase.execute(accessToken)
            emit(Result.success(userProfile))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    // Renovar token
    fun refreshToken(refreshToken: String) = liveData(Dispatchers.IO) {
        try {
            val tokens = refreshAccessTokenUseCase.execute(refreshToken)
            emit(Result.success(tokens))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    // Buscar principais artistas
    fun getTopArtists(accessToken: String) = liveData(Dispatchers.IO) {
        try {
            val topArtists = getTopArtistsUseCase.execute(accessToken)
            emit(Result.success(topArtists))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}

