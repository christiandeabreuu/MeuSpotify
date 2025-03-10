package com.example.spotify.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.spotify.databinding.ActivityLoginBinding
import com.example.spotify.ui.LoginViewModelFactory
import com.example.spotify.ui.artist.ArtistActivity
import com.example.spotify.utils.Constants

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by lazy {
        val factory = LoginViewModelFactory(
            applicationContext
        )
        ViewModelProvider(this, factory)[LoginViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("LoginActivity", "onCreate chamado. Intent data: ${intent?.data}")
        setupButtonListeners()
        handleRedirect(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("LoginActivity", "onNewIntent chamado com URI: ${intent.data}")
        handleRedirect(intent)
    }

    private fun setupButtonListeners() {
        binding.buttonStart.setOnClickListener {
            Log.d("LoginActivity", "Botão de início clicado. Abrindo URL: ${Constants.AUTH_URL}")
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.AUTH_URL))
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.buttonStart.isVisible = !isLoading
        binding.buttonStart.isEnabled = !isLoading
    }

    private fun handleRedirect(intent: Intent?) {
        val uri: Uri? = intent?.data
        Log.d("LoginActivity", "handleRedirect() chamado com URI: $uri")

        if (uri != null && uri.toString().startsWith(Constants.REDIRECT_URI)) {
            Log.d("LoginActivity", "URI inicia com REDIRECT_URI, processando redirecionamento...")
            loginViewModel.handleRedirect(uri, Constants.REDIRECT_URI).observe(this) { result ->
                    result?.onSuccess { tokenState ->
                        tokenState.token?.let { tokens ->

                            Log.d(
                                "LoginActivity",
                                "Tokens recebidos com sucesso: accessToken=${tokens.accessToken}, refreshToken=${tokens.refreshToken}"
                            )
                            loginViewModel.saveTokens(tokens.accessToken, tokens.refreshToken)
                        }
                        showLoading(tokenState.event == TokenStateEvent.Loading)

                        navigateToMainActivity()
                    }?.onFailure { e ->
                        Log.e("LoginActivity", "Erro ao obter token: ${e.message}")
                    }
                }
        } else {
            Log.e(
                "LoginActivity",
                "Redirecionamento inválido ou URI não corresponde ao REDIRECT_URI esperado"
            )
        }
    }

    private fun navigateToMainActivity() {
        Log.d("LoginActivity", "Navegando para ArtistActivity")
        val intent = Intent(this, ArtistActivity::class.java)
        startActivity(intent)
        finish()
    }

}
