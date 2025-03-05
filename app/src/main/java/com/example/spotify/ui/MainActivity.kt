package com.example.spotify.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.spotify.AccessTokenResponse
import com.example.spotify.R
import com.example.spotify.RetrofitInstance
import com.example.spotify.SpotifyTokenService
import com.example.spotify.UserProfile
import com.example.spotify.auth.SpotifyAuthHelper
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private lateinit var accessToken: String
    private lateinit var refreshToken: String
    private lateinit var spotifyAuthHelper: SpotifyAuthHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spotifyAuthHelper = SpotifyAuthHelper(this)
        loadTokens()

        if (accessToken.isNotEmpty()) {
            lifecycleScope.launch {
                try {
                    getUserProfile()
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "Erro: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            navigateToLogin()
        }
    }

    private fun loadTokens() {
        val sharedPreferences = getSharedPreferences("SpotifyPrefs", Context.MODE_PRIVATE)
        accessToken = sharedPreferences.getString("ACCESS_TOKEN", "") ?: ""
        refreshToken = sharedPreferences.getString("REFRESH_TOKEN", "") ?: ""
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private suspend fun getUserProfile() {
        val api = RetrofitInstance.api
        try {
            val userProfile = api.getUserProfile("Bearer $accessToken")
            Toast.makeText(this@MainActivity, "Bem-vindo, ${userProfile.displayName}", Toast.LENGTH_LONG).show()
        } catch (e: HttpException) {
            // Captura erros HTTP (como 401, 404, etc.)
            when (e.code()) {
                401 -> {
                    // Token expirado, renove o token
                    refreshToken()
                    getUserProfile() // Tenta novamente com o novo token
                }
                else -> {
                    Toast.makeText(this@MainActivity, "Falha ao obter perfil: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: IOException) {
            // Captura erros de rede (como timeout, conexão perdida, etc.)
            Toast.makeText(this@MainActivity, "Erro de rede: ${e.message}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            // Captura outros erros inesperados
            Toast.makeText(this@MainActivity, "Erro inesperado: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private suspend fun refreshToken() {
        try {
            val tokens = spotifyAuthHelper.refreshAccessToken(refreshToken)
            saveAccessToken(tokens.accessToken, tokens.refreshToken)
            accessToken = tokens.accessToken
            refreshToken = tokens.refreshToken
        } catch (e: Exception) {
            Log.e("MainActivity", "Erro ao renovar token: ${e.message}")
            navigateToLogin() // Redireciona para a tela de login
        }
    }

    private fun saveAccessToken(accessToken: String, refreshToken: String) {
        val sharedPreferences = getSharedPreferences("SpotifyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("ACCESS_TOKEN", accessToken)
        editor.putString("REFRESH_TOKEN", refreshToken)
        editor.apply()
    }
}





