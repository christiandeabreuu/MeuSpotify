package com.example.spotify.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spotify.R
import com.example.spotify.auth.SpotifyAuthHelper

class LoginActivity : AppCompatActivity() {
    private lateinit var spotifyAuthHelper: SpotifyAuthHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        spotifyAuthHelper = SpotifyAuthHelper(this)

        // Iniciar autenticação
        spotifyAuthHelper.authenticate(
            onSuccess = { accessToken ->
                // Salvando token
                val sharedPrefs = getSharedPreferences("SPOTIFY", Context.MODE_PRIVATE)
                with(sharedPrefs.edit()) {
                    putString("ACCESS_TOKEN", accessToken)
                    apply()
                }

                // Navegação para MainActivity
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("ACCESS_TOKEN", accessToken)
                startActivity(intent)
                finish()
            },
            onError = { error ->
                // Tratar o erro de autenticação
                Toast.makeText(this, "Erro de autenticação: $error", Toast.LENGTH_SHORT).show()
            }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        spotifyAuthHelper.handleResponse(requestCode, resultCode, data,
            onSuccess = { accessToken ->
                // Salvando token
                val sharedPrefs = getSharedPreferences("SPOTIFY", Context.MODE_PRIVATE)
                with(sharedPrefs.edit()) {
                    putString("ACCESS_TOKEN", accessToken)
                    apply()
                }

                // Navegação para MainActivity
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("ACCESS_TOKEN", accessToken)
                startActivity(intent)
                finish()
            },
            onError = { error ->
                // Tratar o erro de autenticação
                Toast.makeText(this, "Erro de autenticação: $error", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

