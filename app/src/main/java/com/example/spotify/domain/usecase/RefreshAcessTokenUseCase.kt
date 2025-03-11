package com.example.spotify.domain.usecase

import com.example.spotify.data.repository.AuthRepository
import com.example.spotify.data.model.Tokens


class RefreshAccessTokenUseCase(private val repository: AuthRepository) {
    suspend fun execute(refreshToken: String): Tokens? {
        return try {
            val tokens = repository.refreshAccessToken(refreshToken)
            tokens
        } catch (e: Exception) {
            null
        }
    }
}


