package com.example.spotify.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.spotify.Constants.REDIRECT_URI
import com.example.spotify.R
import com.example.spotify.auth.SpotifyAuthHelper

class LoginActivity : AppCompatActivity() {
    private lateinit var spotifyAuthHelper: SpotifyAuthHelper

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        spotifyAuthHelper = SpotifyAuthHelper(this)

        val uri = intent.data
        if (uri != null && uri.toString().startsWith(REDIRECT_URI)) {
            val code = uri.getQueryParameter("code")
            if (code != null) {
                spotifyAuthHelper.getAccessToken(code, onSuccess = { accessToken, refreshToken ->
                    val sharedPrefs = getSharedPreferences("SPOTIFY", Context.MODE_PRIVATE)
                    with(sharedPrefs.edit()) {
                        putString("ACCESS_TOKEN", accessToken)
                        putString("REFRESH_TOKEN", refreshToken)
                        apply()
                    }
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("ACCESS_TOKEN", accessToken)
                    startActivity(intent)
                    finish()
                }, onError = { error ->
                    Toast.makeText(this, "Erro de autenticação: $error", Toast.LENGTH_SHORT).show()
                })
            } else {
                Toast.makeText(this, "Erro: código de autorização não encontrado", Toast.LENGTH_SHORT).show()
            }
        } else {
            spotifyAuthHelper.redirectToLogin()
        }
    }
}

