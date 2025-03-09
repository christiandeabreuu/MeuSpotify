package com.example.spotify.domain.usecase

import android.util.Log
import com.example.spotify.data.repository.AuthRepository
import com.example.spotify.data.model.SpotifyTokens.Tokens


class RefreshAccessTokenUseCase(private val repository: AuthRepository) {
    suspend fun execute(refreshToken: String): Tokens? {
        Log.d("RefreshAccessTokenUseCase", "execute chamado com refreshToken: $refreshToken")
        return try {
            val tokens = repository.refreshAccessToken(refreshToken)
            Log.d("RefreshAccessTokenUseCase", "Tokens renovados com sucesso: accessToken=${tokens.accessToken}, refreshToken=${tokens.refreshToken}")
            tokens
        } catch (e: Exception) {
            Log.e("RefreshAccessTokenUseCase", "Erro ao renovar token: ${e.message}")
            null
        }
    }
}


