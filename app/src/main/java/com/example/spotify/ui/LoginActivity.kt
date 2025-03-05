package com.example.spotify.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.spotify.auth.SpotifyAuthHelper
import com.example.spotify.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var spotifyAuthHelper: SpotifyAuthHelper

    companion object {
        private const val CLIENT_ID = "9cde7198eaf54c06860b6d0257dcd893"
        private const val REDIRECT_URI = "meuapp://callback"
        private const val AUTH_URL =
            "https://accounts.spotify.com/authorize" + "?client_id=$CLIENT_ID" + "&response_type=code" + "&redirect_uri=$REDIRECT_URI" + "&scope=user-read-private%20user-read-email" // Escopos necessários
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spotifyAuthHelper = SpotifyAuthHelper(this)
        buttonStart()

        lifecycleScope.launch {
            handleRedirect(intent)
        }
    }

    private fun buttonStart() {
        binding.buttonStart.setOnClickListener {
            // Abre a URL de autenticação do Spotify
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(AUTH_URL))
            startActivity(intent)
        }
    }

    private suspend fun handleRedirect(intent: Intent?) {
        val uri = intent?.data
        if (uri != null && uri.toString().startsWith(REDIRECT_URI)) {
            // Captura o código de autorização da URL
            val authorizationCode = uri.getQueryParameter("code")
            if (authorizationCode != null) {
                // Troca o código de autorização por um access token
                obtainTokens(authorizationCode)
            } else {
                Log.e("LoginActivity", "Código de autorização não encontrado na URL")
                Toast.makeText(
                    this,
                    "Erro: Código de autorização não encontrado",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private suspend fun obtainTokens(authorizationCode: String) {
        try {
            val tokens = spotifyAuthHelper.getAccessToken(authorizationCode)
            saveTokens(tokens.accessToken, tokens.refreshToken)
            navigateToMainActivity()
        } catch (e: Exception) {
            Log.e("LoginActivity", "Erro ao obter token de acesso: ${e.message}")
            Toast.makeText(this, "Erro: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveTokens(accessToken: String, refreshToken: String) {
        val sharedPreferences = getSharedPreferences("SpotifyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("ACCESS_TOKEN", accessToken)
        editor.putString("REFRESH_TOKEN", refreshToken)
        editor.apply()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
