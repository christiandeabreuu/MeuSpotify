package com.example.spotify.data.repository

import com.example.spotify.auth.SpotifyAuthHelper
import com.example.spotify.auth.SpotifyAuthHelper.Tokens

class AuthRepositoryImpl(private val authHelper: SpotifyAuthHelper) : AuthRepository {
    override suspend fun getAccessToken(authorizationCode: String): Tokens {
        return authHelper.getAccessToken(authorizationCode)
    }

    override suspend fun refreshAccessToken(refreshToken: String): Tokens {
        return authHelper.refreshAccessToken(refreshToken)
    }
}
