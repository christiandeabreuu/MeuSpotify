package com.example.spotify.domain.usecase

import com.example.spotify.data.repository.AuthRepository
import com.example.spotify.data.model.Tokens

class GetAccessTokenUseCase(private val repository: AuthRepository) {
    suspend fun execute(authorizationCode: String, redirectUri: String): Tokens {
        return repository.getAccessToken(authorizationCode, redirectUri)
    }
}

