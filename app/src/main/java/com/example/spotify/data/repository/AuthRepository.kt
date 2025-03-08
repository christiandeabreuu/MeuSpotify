package com.example.spotify.data.repository


import com.example.spotify.data.model.SpotifyTokens.Tokens

interface AuthRepository {
    suspend fun getAccessToken(authorizationCode: String): Tokens
    suspend fun refreshAccessToken(refreshToken: String): Tokens
    fun saveTokens(accessToken: String, refreshToken: String)
    fun loadTokens(): Pair<String, String>
}
