package com.example.spotify.data.repository

import android.content.Context
import com.example.spotify.auth.SpotifyAuthHelper
import com.example.spotify.auth.SpotifyAuthHelper.Tokens

class AuthRepositoryImpl(
    private val context: Context,
    private val authHelper: SpotifyAuthHelper
) : AuthRepository {

    private val sharedPreferences = context.getSharedPreferences("SpotifyPrefs", Context.MODE_PRIVATE)

    override suspend fun getAccessToken(authorizationCode: String): Tokens {
        return authHelper.getAccessToken(authorizationCode)
    }

    override suspend fun refreshAccessToken(refreshToken: String): Tokens {
        return authHelper.refreshAccessToken(refreshToken)
    }

    override fun saveTokens(accessToken: String, refreshToken: String) {
        val editor = sharedPreferences.edit()
        editor.putString("ACCESS_TOKEN", accessToken)
        editor.putString("REFRESH_TOKEN", refreshToken)
        editor.apply()
    }

    override fun loadTokens(): Pair<String, String> {
        val accessToken = sharedPreferences.getString("ACCESS_TOKEN", "") ?: ""
        val refreshToken = sharedPreferences.getString("REFRESH_TOKEN", "") ?: ""
        return Pair(accessToken, refreshToken)
    }
}
