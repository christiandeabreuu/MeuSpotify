package com.example.spotify.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.spotify.R
import com.example.spotify.auth.SpotifyAuthHelper
import com.spotify.sdk.android.auth.AccountsQueryParameters.CLIENT_ID
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse

class LoginActivity : AppCompatActivity() {
    private lateinit var spotifyAuthHelper: SpotifyAuthHelper

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        spotifyAuthHelper = SpotifyAuthHelper(this)

          val REQUEST_CODE = 1337
          val REDIRECT_URI = "meuapp://callback"

        val builder = AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.CODE, REDIRECT_URI)

        builder.setScopes(arrayOf("streaming"))
        val request = builder.build()

        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request)


        // Iniciar autenticação
//        Log.d("LoginActivity", "Iniciando autenticação...")
//        spotifyAuthHelper.getAccessToken(
//            onSuccess = { accessToken ->
//                Log.d("LoginActivity", "Token de acesso obtido com sucesso: $accessToken")
//                // Salvando token
//                val sharedPrefs = getSharedPreferences("SPOTIFY", Context.MODE_PRIVATE)
//                with(sharedPrefs.edit()) {
//                    putString("ACCESS_TOKEN", accessToken)
//                    apply()
//                }
//
//                // Navegação para MainActivity
//                val intent = Intent(this, MainActivity::class.java)
//                intent.putExtra("ACCESS_TOKEN", accessToken)
//                startActivity(intent)
//                finish()
//            },
//            onError = { error ->
//                Log.e("LoginActivity", "Erro de autenticação: $error")
//                Toast.makeText(this, "Erro de autenticação: $error", Toast.LENGTH_SHORT).show()
//            }
//        )
    }
}