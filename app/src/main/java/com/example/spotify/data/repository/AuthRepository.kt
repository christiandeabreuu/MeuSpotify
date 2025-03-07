package com.example.spotify.data.repository

import com.example.spotify.auth.SpotifyAuthHelper.Tokens

interface AuthRepository {
    suspend fun getAccessToken(authorizationCode: String): Tokens
    suspend fun refreshAccessToken(refreshToken: String): Tokens
}
