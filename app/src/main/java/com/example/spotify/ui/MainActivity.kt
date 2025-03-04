package com.example.spotify.ui


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.spotify.R
import com.example.spotify.RetrofitInstance
import com.example.spotify.auth.SpotifyAuthHelper
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var accessToken: String
    private lateinit var refreshToken: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Recupera o token de acesso
        accessToken = intent.getStringExtra("ACCESS_TOKEN") ?: run {
            Log.e("MainActivity", "Token de acesso não encontrado")
            Toast.makeText(this, "Erro: token de acesso não encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Verifica a validade do token antes de fazer chamadas à API
        checkTokenValidity()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkTokenValidity() {
        val sharedPrefs = getSharedPreferences("SPOTIFY", Context.MODE_PRIVATE)
        val tokenTimestamp = sharedPrefs.getLong("TOKEN_TIMESTAMP", 0)

        if (isTokenExpired(tokenTimestamp)) {
            // Token expirado, tenta renovar
            val refreshToken = sharedPrefs.getString("REFRESH_TOKEN", null)
            if (refreshToken != null) {
                refreshAccessToken(refreshToken)
            } else {
                Toast.makeText(this, "Erro: refresh token não encontrado", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            // Token válido, faz as chamadas à API
            getUserProfile()
            getTopArtists()
        }
    }

    private fun isTokenExpired(tokenTimestamp: Long): Boolean {
        val currentTime = System.currentTimeMillis()
        return (currentTime - tokenTimestamp) >= 3600 * 1000 // 1 hora em milissegundos
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun refreshAccessToken(refreshToken: String) {
        val spotifyAuthHelper = SpotifyAuthHelper(this)
        spotifyAuthHelper.refreshAccessToken(refreshToken, onSuccess = { newAccessToken, newRefreshToken ->
            val sharedPrefs = getSharedPreferences("SPOTIFY", Context.MODE_PRIVATE)
            with(sharedPrefs.edit()) {
                putString("ACCESS_TOKEN", newAccessToken)
                putLong("TOKEN_TIMESTAMP", System.currentTimeMillis())

                // Se um novo refreshToken for retornado, salve-o
                if (newRefreshToken.isNotEmpty()) {
                    putString("REFRESH_TOKEN", newRefreshToken)
                }

                apply()
            }
            accessToken = newAccessToken

            // Após renovar o token, faz as chamadas à API
            getUserProfile()
            getTopArtists()
        }, onError = { error ->
            Toast.makeText(this, "Erro ao renovar token: $error", Toast.LENGTH_SHORT).show()
            finish()
        })
    }

    private fun getUserProfile() {
        lifecycleScope.launch {
            val result = runCatching { RetrofitInstance.api.getUserProfile("Bearer $accessToken") }
            result.onSuccess { userProfile ->
                Toast.makeText(this@MainActivity, "Bem-vindo, ${userProfile.displayName}", Toast.LENGTH_LONG).show()
            }.onFailure { e ->
                Toast.makeText(this@MainActivity, "Erro ao buscar perfil: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getTopArtists() {
        lifecycleScope.launch {
            val result = runCatching { RetrofitInstance.api.getTopArtists("Bearer $accessToken") }
            result.onSuccess { topArtists ->
                if (topArtists.isSuccessful && topArtists.body() != null) {
                    Log.d("TopArtist", "Resposta da API: ${topArtists.body()}")
                    topArtists.body()!!.items.forEach { artist ->
                        Log.d("TopArtist", "Artista: ${artist.name}")
                    }
                } else {
                    Log.d("TopArtist", "Nenhum artista encontrado.")
                    Log.d("TopArtist", "Código de status: ${topArtists.code()}")
                    Log.d("TopArtist", "Mensagem de erro: ${topArtists.message()}")
                    Log.d("TopArtist", "Corpo do erro: ${topArtists.errorBody()?.string()}")
                }
            }.onFailure { e ->
                Log.e("TopArtist", "Erro ao buscar top artists: ${e.message}")
                Toast.makeText(this@MainActivity, "Erro ao buscar top artists: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}

