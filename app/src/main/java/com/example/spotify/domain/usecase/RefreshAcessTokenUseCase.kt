package com.example.spotify.domain.usecase

import com.example.spotify.data.repository.AuthRepository
import com.example.spotify.data.model.SpotifyTokens.Tokens

class RefreshAccessTokenUseCase(private val repository: AuthRepository) {
    suspend fun execute(refreshToken: String): Tokens? {
        return try {
            repository.refreshAccessToken(refreshToken)
        } catch (e: Exception) {
            null
        }
    }
}

