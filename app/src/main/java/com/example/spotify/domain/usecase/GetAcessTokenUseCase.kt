package com.example.spotify.domain.usecase


import com.example.spotify.auth.SpotifyAuthHelper.Tokens
import com.example.spotify.data.repository.AuthRepository

class GetAccessTokenUseCase(private val repository: AuthRepository) {
    suspend fun execute(authorizationCode: String): Tokens {
        return repository.getAccessToken(authorizationCode)
    }
}
