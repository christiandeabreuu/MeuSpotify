package com.example.spotify.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.spotify.AccessTokenResponse
import com.example.spotify.auth.SpotifyAuthHelper
import com.example.spotify.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var spotifyAuthHelper: SpotifyAuthHelper

    companion object {
        private const val CLIENT_ID = "9cde7198eaf54c06860b6d0257dcd893"
        private const val REDIRECT_URI = "meuapp://callback"
        private const val AUTH_URL =
            "https://accounts.spotify.com/authorize?client_id=$CLIENT_ID&response_type=code&redirect_uri=$REDIRECT_URI&scope=user-read-private%20user-read-email" // Escopos necessários
    }

    // Inicializa a ViewModel usando a ViewModelFactory
    private val loginViewModel: LoginViewModel by viewModels { LoginViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spotifyAuthHelper = SpotifyAuthHelper(this)
        buttonStart()

        handleRedirect(intent)
    }

    private fun buttonStart() {
        binding.buttonStart.setOnClickListener {
            // Abre a URL de autenticação do Spotify
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(AUTH_URL))
            startActivity(intent)
        }
    }

    private fun handleRedirect(intent: Intent?) {
        val uri = intent?.data
        if (uri != null && uri.toString().startsWith(REDIRECT_URI)) {
            loginViewModel.handleRedirect(uri, REDIRECT_URI).observe(this, Observer { result ->
                result?.let {
                    it.onSuccess { tokens ->
                        if (tokens is AccessTokenResponse) {
                            saveTokens(tokens.accessToken, tokens.refreshToken)
                            navigateToMainActivity()
                        }
                    }.onFailure { e ->
                        Log.e("LoginActivity", "Erro ao obter token de acesso: ${e.message}")
                        Toast.makeText(this, "Erro: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            })
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
