package com.example.spotify.domain.usecase

import com.example.spotify.auth.AuthRepository
import com.example.spotify.auth.SpotifyAuthHelper.Tokens

class RefreshAccessTokenUseCase(private val repository: AuthRepository) {
    suspend fun execute(refreshToken: String): Tokens? {
        return try {
            repository.refreshAccessToken(refreshToken)
        } catch (e: Exception) {
            null
        }
    }
}

