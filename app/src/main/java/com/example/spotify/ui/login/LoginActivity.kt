package com.example.spotify.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.spotify.databinding.ActivityLoginBinding
import com.example.spotify.domain.usecase.GetAccessTokenUseCase
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

    // Se a activity já estiver ativa e receber uma nova Intent, ela entrará aqui.
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("LoginActivity", "onNewIntent chamado com URI: ${intent?.data}")
        handleRedirect(intent)
    }

    private fun setupButtonListeners() {
        binding.buttonStart.setOnClickListener {
            Log.d("LoginActivity", "Botão de início clicado. Abrindo URL: ${Constants.AUTH_URL}")
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.AUTH_URL))
            startActivity(intent)
        }
    }

    private fun handleRedirect(intent: Intent?) {
        // Exibe o dado recebido na Intent
        val uri: Uri? = intent?.data
        Log.d("LoginActivity", "handleRedirect() chamado com URI: $uri")

        // Verifica se a URI não é nula e corresponde ao seu redirect URI
        if (uri != null && uri.toString().startsWith(Constants.REDIRECT_URI)) {
            Log.d("LoginActivity", "URI inicia com REDIRECT_URI, processando redirecionamento...")
            loginViewModel.handleRedirect(uri, Constants.REDIRECT_URI)
                .observe(this) { result ->
                    result?.onSuccess { tokens ->
                        Log.d("LoginActivity", "Tokens recebidos com sucesso: accessToken=${tokens.accessToken}, refreshToken=${tokens.refreshToken}")
                        loginViewModel.saveTokens(tokens.accessToken, tokens.refreshToken)

                        // Navegar automaticamente para a ArtistActivity
                        navigateToMainActivity()
                    }?.onFailure { e ->
                        Log.e("LoginActivity", "Erro ao obter token: ${e.message}")
                    }
                }
        } else {
            Log.e("LoginActivity", "Redirecionamento inválido ou URI não corresponde ao REDIRECT_URI esperado")
        }
    }


    private fun navigateToMainActivity() {
        Log.d("LoginActivity", "Navegando para ArtistActivity")
        val intent = Intent(this, ArtistActivity::class.java)
        startActivity(intent)
        finish() // Finaliza a tela atual para não voltar ao login.
    }

}
