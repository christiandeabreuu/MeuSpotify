package com.example.spotify.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.spotify.auth.SpotifyAuthHelper
import com.example.spotify.data.model.AccessTokenResponse
import kotlinx.coroutines.Dispatchers

class LoginViewModel(private val context: Context) : ViewModel() {
    private val spotifyAuthHelper = SpotifyAuthHelper(context)

    fun obtainTokens(authorizationCode: String) = liveData(Dispatchers.IO) {
        try {
            val tokens = spotifyAuthHelper.getAccessToken(authorizationCode)
            emit(Result.success(tokens))
        } catch (e: Exception) {
            emit(Result.failure<AccessTokenResponse>(e))
        }
    }

    fun handleRedirect(uri: Uri?, redirectUri: String) = liveData(Dispatchers.IO) {
        if (uri != null && uri.toString().startsWith(redirectUri)) {
            val authorizationCode = uri.getQueryParameter("code")
            if (authorizationCode != null) {
                emit(obtainTokens(authorizationCode).value)
            } else {
                emit(Result.failure<AccessTokenResponse>(Exception("Código de autorização não encontrado")))
            }
        } else {
            emit(Result.failure<AccessTokenResponse>(Exception("URI inválida ou null")))
        }
    }
}
