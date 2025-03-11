package com.example.spotify.domain.usecase


import android.util.Log
import com.example.spotify.data.model.Tokens
import com.example.spotify.data.repository.AuthRepository

class GetAccessTokenUseCase(private val repository: AuthRepository) {
    suspend fun execute(authorizationCode: String, redirectUri: String): Tokens {
        Log.d(
            "GetAccessTokenUseCase",
            "execute chamado com authorizationCode: $authorizationCode e redirectUri: $redirectUri"
        )
        return repository.getAccessToken(authorizationCode, redirectUri)
    }
}

