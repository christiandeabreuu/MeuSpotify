package com.example.spotify.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.spotify.data.model.AccessTokenResponse
import com.example.spotify.auth.SpotifyAuthHelper
import com.example.spotify.utils.Constants
import kotlinx.coroutines.Dispatchers

class LoginViewModel(private val context: Context) : ViewModel() {

    private val spotifyAuthHelper: SpotifyAuthHelper = SpotifyAuthHelper(context)

    fun handleRedirect(uri: Uri, redirectUri: String) = liveData(Dispatchers.IO) {
        val authorizationCode = uri.getQueryParameter("code")
        if (authorizationCode != null) {
            try {
                val tokens = spotifyAuthHelper.getAccessToken(authorizationCode)
                emit(Result.success(tokens))
            } catch (e: Exception) {
                emit(Result.failure<AccessTokenResponse>(e))
            }
        } else {
            emit(Result.failure<AccessTokenResponse>(Exception("Código de autorização não encontrado")))
        }
    }

    fun saveTokens(accessToken: String, refreshToken: String) {
        val sharedPreferences = context.getSharedPreferences("SpotifyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("ACCESS_TOKEN", accessToken)
        editor.putString("REFRESH_TOKEN", refreshToken)
        editor.apply()
    }
}
