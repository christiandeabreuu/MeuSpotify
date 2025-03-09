package com.example.spotify.data.repository


import com.example.spotify.data.model.SpotifyTokens.Tokens

interface AuthRepository {
    // Agora o método getAccessToken recebe também o redirectUri, o que é necessário para
    // fazer a requisição com todos os parâmetros exigidos pela API do Spotify.
    suspend fun getAccessToken(authorizationCode: String, redirectUri: String): Tokens

    suspend fun refreshAccessToken(refreshToken: String): Tokens

    fun saveTokens(accessToken: String, refreshToken: String)

    fun loadTokens(): Pair<String, String>
}

