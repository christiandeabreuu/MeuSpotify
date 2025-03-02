package com.example.spotify.ui


import android.content.Context
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.spotify.R
import com.example.spotify.RetrofitInstance
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var accessToken: String
    private lateinit var refreshToken: String

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

        // Faz as chamadas à API
        getTopArtists()
        getUserProfile()
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



