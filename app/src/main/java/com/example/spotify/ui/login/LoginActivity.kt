package com.example.spotify.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.spotify.data.model.AccessTokenResponse
import com.example.spotify.databinding.ActivityLoginBinding
import com.example.spotify.ui.artist.ArtistActivity
import com.example.spotify.ui.LoginViewModel
import com.example.spotify.ui.LoginViewModelFactory
import com.example.spotify.utils.Constants

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels { LoginViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtonListeners()
        handleRedirect(intent)
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
                            loginViewModel.saveTokens(tokens.accessToken, tokens.refreshToken)
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

    private fun navigateToMainActivity() {
        val intent = Intent(this, ArtistActivity::class.java)
        startActivity(intent)
        finish()
    }
}
