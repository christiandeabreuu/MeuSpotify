package com.example.spotify.domain.usecase

import com.example.spotify.auth.AuthRepository
import com.example.spotify.auth.SpotifyAuthHelper.Tokens

class GetAccessTokenUseCase(private val repository: AuthRepository) {
    suspend fun execute(authorizationCode: String): Tokens {
        return repository.getAccessToken(authorizationCode)
    }
}
