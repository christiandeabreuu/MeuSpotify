package com.example.spotify.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.spotify.SpotifyApiService
import com.example.spotify.UserProfile
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import retrofit2.Call


class SpotifyAuthHelper(private val context: Context) : SpotifyApiService {

    private val CLIENT_ID = "9cde7198eaf54c06860b6d0257dcd893"
    private val REDIRECT_URI = "meuapp://callback"
    private val REQUEST_CODE = 1337
    private val CLIENT_SECRET = "d601127a963c4791a61e9145bedd7fe6" //guardar

    override fun authenticate(onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        // Simulação de uma chamada de autenticação
        val isSuccess = true // Apenas para exemplo

        if (isSuccess) {
            val accessToken = "dummy_access_token"
            onSuccess(accessToken)
        } else {
            val error = "Authentication failed"
            onError(error)
        }
    }

    override fun getUserProfile(accessToken: String): Call<UserProfile> {
        TODO("Not yet implemented")
    }

    override fun handleResponse(requestCode: Int, resultCode: Int, data: Intent?, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        if (resultCode == Activity.RESULT_OK) {
            val accessToken = "dummy_access_token" // Exemplo de token
            onSuccess(accessToken)
        } else {
            val error = "Authentication failed"
            onError(error)
        }
    }
}

