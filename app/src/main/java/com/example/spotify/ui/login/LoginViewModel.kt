package com.example.spotify.ui.login

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.spotify.data.model.Tokens
import com.example.spotify.domain.usecase.GetAccessTokenUseCase
import kotlinx.coroutines.Dispatchers

class LoginViewModel(
    private val context: Context, private val getAccessTokenUseCase: GetAccessTokenUseCase
) : ViewModel() {

    fun handleRedirect(uri: Uri, redirectUri: String) = liveData(Dispatchers.IO) {
        val authorizationCode = uri.getQueryParameter("code")
        if (authorizationCode != null) {
            try {
                Log.d(
                    "LoginViewModel",
                    "Chamando getAccessToken com authorizationCode: $authorizationCode e redirectUri: $redirectUri"
                )
                emit(Result.success(TokenState()))
                val tokens: Tokens = getAccessTokenUseCase.execute(authorizationCode, redirectUri)
                Log.d(
                    "LoginViewModel",
                    "Tokens obtidos: accessToken=${tokens.accessToken}, refreshToken=${tokens.refreshToken}"
                )
                emit(Result.success(TokenState(tokens, TokenStateEvent.GetToken)))
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Erro ao trocar código pelos tokens: ${e.message}")
                emit(Result.failure(e))
            }
        } else {
            Log.e("LoginViewModel", "Código de autorização não encontrado na URI")
            emit(Result.failure(Exception("Código de autorização não encontrado")))
        }
    }

    fun saveTokens(accessToken: String, refreshToken: String) {
        val sharedPreferences = context.getSharedPreferences("SpotifyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("ACCESS_TOKEN", accessToken)
        editor.putString("REFRESH_TOKEN", refreshToken)
        editor.apply()
        Log.d(
            "LoginViewModel",
            "Tokens salvos localmente: accessToken=$accessToken, refreshToken=$refreshToken"
        )
    }
}
