package com.example.spotify.domain.usecase

import com.example.spotify.auth.SpotifyAuthHelper.Tokens
import com.example.spotify.data.repository.AuthRepository

class RefreshAccessTokenUseCase(private val repository: AuthRepository) {
    suspend fun execute(refreshToken: String): Tokens {
        return repository.refreshAccessToken(refreshToken)
    }
}
