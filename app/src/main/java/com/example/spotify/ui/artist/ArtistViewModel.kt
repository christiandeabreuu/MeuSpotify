package com.example.spotify.ui.artist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.spotify.data.model.SpotifyTokens
import com.example.spotify.domain.usecase.*
import kotlinx.coroutines.Dispatchers

class ArtistViewModel(
    private val loadTokensUseCase: LoadTokensUseCase,
    private val saveTokensUseCase: SaveTokensUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val refreshAccessTokenUseCase: RefreshAccessTokenUseCase,
    private val getTopArtistsUseCase: GetTopArtistsUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase
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

    fun exchangeCodeForTokens(authorizationCode: String, redirectUri: String) = liveData(Dispatchers.IO) {
        try {
            Log.d(
                "ArtistViewModel",
                "Chamando getAccessToken com authorizationCode: $authorizationCode e redirectUri: $redirectUri"
            )
            val tokens: SpotifyTokens.Tokens = getAccessTokenUseCase.execute(authorizationCode, redirectUri)
            Log.d(
                "ArtistViewModel",
                "Tokens obtidos: accessToken=${tokens.accessToken}, refreshToken=${tokens.refreshToken}"
            )
            emit(Result.success(tokens))
        } catch (e: Exception) {
            Log.e("ArtistViewModel", "Erro ao trocar código pelos tokens: ${e.message}")
            emit(Result.failure<SpotifyTokens.Tokens>(e))
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

