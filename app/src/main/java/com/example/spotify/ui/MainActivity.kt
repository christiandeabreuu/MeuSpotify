package com.example.spotify.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.spotify.AccessTokenResponse
import com.example.spotify.R
import com.example.spotify.RetrofitInstance
import com.example.spotify.RetrofitInstance.api
import com.example.spotify.SpotifyApiService
import com.example.spotify.SpotifyTokenService
import com.example.spotify.TopArtistsResponse
import com.example.spotify.UserProfile
import com.example.spotify.auth.SpotifyAuthHelper
import com.example.spotify.databinding.ActivityLoginBinding
import com.example.spotify.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private lateinit var accessToken: String
    private lateinit var refreshToken: String
    private lateinit var spotifyAuthHelper: SpotifyAuthHelper
    private lateinit var spotifyApiService: SpotifyApiService
    private lateinit var artistAdapter: ArtistAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spotifyAuthHelper = SpotifyAuthHelper(this)
        loadTokens()

        recyclerView = findViewById(R.id.artistasRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (accessToken.isNotEmpty()) {
            lifecycleScope.launch {
                try {
                    // Busca o perfil do usuário
                    getUserProfile()
                    // Busca os top artists
                    getTopArtists()
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "Erro: ${e.message}", Toast.LENGTH_LONG)
                        .show()
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
            userProfile?.let {
                // Carrega a imagem de perfil usando Coil
                imageProfile(it.images.firstOrNull()?.url)
            }
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> {
                    refreshToken()
                    getUserProfile()
                }

                else -> {
                    Toast.makeText(
                        this@MainActivity,
                        "Falha ao obter perfil: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        } catch (e: IOException) {
            Toast.makeText(this@MainActivity, "Erro de rede: ${e.message}", Toast.LENGTH_LONG)
                .show()
        } catch (e: Exception) {
            Toast.makeText(this@MainActivity, "Erro inesperado: ${e.message}", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun imageProfile(imageUrl: String?) {
        imageUrl?.let {
            binding.profileImageView.load(it) {
                transformations(CircleCropTransformation())
                placeholder(R.drawable.ic_launcher_background) // Opcional: imagem de placeholder
                error(R.drawable.ic_launcher_foreground) // Opcional: imagem de erro
            }
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
            navigateToLogin()
        }
    }

    private fun saveAccessToken(accessToken: String, refreshToken: String) {
        val sharedPreferences = getSharedPreferences("SpotifyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("ACCESS_TOKEN", accessToken)
        editor.putString("REFRESH_TOKEN", refreshToken)
        editor.apply()
    }

    private suspend fun getTopArtists() {
        spotifyApiService = RetrofitInstance.api
        val call = spotifyApiService.getTopArtists("Bearer $accessToken")

        try {
            val response = call.awaitResponse()
            if (response.isSuccessful) {
                val topArtists = response.body()?.items
                topArtists?.let {
                    artistAdapter = ArtistAdapter(it)
                    recyclerView.adapter = artistAdapter
                }
            } else {
                if (response.code() == 401) {
                    refreshToken()
                    getTopArtists()
                } else {
                    println("Erro ao obter os artistas mais ouvidos: ${response.errorBody()?.string()}")
                }
            }
        } catch (e: IOException) {
            println("Falha na requisição: ${e.message}")
        } catch (e: HttpException) {
            println("Erro HTTP: ${e.message}")
        }
    }
}






