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
import com.example.spotify.data.AccessTokenResponse
import com.example.spotify.auth.SpotifyAuthHelper
import com.example.spotify.databinding.ActivityLoginBinding
import com.example.spotify.utils.Constants

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var spotifyAuthHelper: SpotifyAuthHelper

    // Inicializa a ViewModel usando a ViewModelFactory
    private val loginViewModel: LoginViewModel by viewModels { LoginViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeAuthHelper()
        setupButtonListeners()
        handleRedirect(intent)
    }

    private fun initializeAuthHelper() {
        spotifyAuthHelper = SpotifyAuthHelper(this)
    }

    private fun setupButtonListeners() {
        binding.buttonStart.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.AUTH_URL))
            startActivity(intent)
        }
    }

    private fun handleRedirect(intent: Intent?) {
        val uri = intent?.data
        if (uri != null && uri.toString().startsWith(Constants.REDIRECT_URI)) {
            loginViewModel.handleRedirect(uri, Constants.REDIRECT_URI).observe(this, Observer { result ->
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
        val intent = Intent(this, ArtistActivity::class.java)
        startActivity(intent)
        finish()
    }
}
