package com.example.spotify.ui.login

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
        setupButtonListeners()
        handleRedirect(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleRedirect(intent)
    }

    private fun setupButtonListeners() {
        binding.buttonStart.setOnClickListener {
            if (!isInternetAvailable()) {
                Toast.makeText(
                    this,
                    "Sem conexão com a internet. Carregando dados offline.",
                    Toast.LENGTH_SHORT
                ).show()
                navigateToMainActivity()
            } else {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.AUTH_URL))
                startActivity(intent)
            }
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
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
            loginViewModel.handleRedirect(uri, Constants.REDIRECT_URI).observe(this) { result ->
                result?.onSuccess { tokenState ->
                    tokenState.token?.let { tokens ->
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
