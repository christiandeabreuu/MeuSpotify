package com.example.spotify.data.repository


import com.example.spotify.data.model.Tokens

interface AuthRepository {
    suspend fun getAccessToken(authorizationCode: String, redirectUri: String): Tokens

    suspend fun refreshAccessToken(refreshToken: String): Tokens

    fun saveTokens(accessToken: String, refreshToken: String)

    fun loadTokens(): Pair<String, String>
}

