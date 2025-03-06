package com.example.spotify.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.spotify.Constants.CLIENT_SECRET
import com.example.spotify.PKCEUtils
import com.example.spotify.SpotifyTokenService
import com.example.spotify.auth.SpotifyAuthHelper
import com.example.spotify.databinding.ActivityLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var spotifyAuthHelper: SpotifyAuthHelper

    companion object {
        private const val CLIENT_ID = "9cde7198eaf54c06860b6d0257dcd893"
        private const val REDIRECT_URI = "meuapp://callback"
        private const val AUTH_URL =
            "https://accounts.spotify.com/authorize" +
                    "?client_id=$CLIENT_ID" +
                    "&response_type=code" +
                    "&redirect_uri=$REDIRECT_URI" +
                    "&scope=user-read-private%20user-read-email%20user-top-read" // Incluindo escopo necessário
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spotifyAuthHelper = SpotifyAuthHelper(this)
        buttonStart()

        lifecycleScope.launch {
            handleRedirect(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun buttonStart() {
        binding.buttonStart.setOnClickListener {

            // Gera o code_verifier e o code_challenge
            val codeVerifier = PKCEUtils.generateCodeVerifier()
            val codeChallenge = PKCEUtils.generateCodeChallenge(codeVerifier)

            // Salva o code_verifier no SharedPreferences
            val sharedPreferences = getSharedPreferences("SpotifyPrefs", Context.MODE_PRIVATE)
            sharedPreferences.edit().putString("CODE_VERIFIER", codeVerifier).apply()

            // Constrói a URL de autenticação com o code_challenge
            val authUrl = buildAuthUrl(codeChallenge)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(AUTH_URL))
            startActivity(intent)
        }
    }

    private fun buildAuthUrl(codeChallenge: String): String {
        return "https://accounts.spotify.com/authorize" +
                "?client_id=$CLIENT_ID" +
                "&response_type=code" +
                "&redirect_uri=$REDIRECT_URI" +
                "&scope=user-read-private%20user-read-email%20user-top-read" + // Adicione os escopos necessários
                "&code_challenge_method=S256" +
                "&code_challenge=$codeChallenge"
    }

    private suspend fun handleRedirect(intent: Intent?) {
        val uri = intent?.data
        if (uri != null && uri.toString().startsWith(REDIRECT_URI)) {
            // Captura o código de autorização da URL
            val authorizationCode = uri.getQueryParameter("code")
            if (authorizationCode != null) {
                // Recupera o code_verifier salvo
                val codeVerifier = getSharedPreferences("SpotifyPrefs", Context.MODE_PRIVATE)
                    .getString("CODE_VERIFIER", null)

                if (codeVerifier != null) {
                    // Troca o código de autorização por um access token
                    obtainTokens(authorizationCode, codeVerifier)
                } else {
                    Log.e("LoginActivity", "Code verifier não encontrado")
                    Toast.makeText(this, "Erro: Code verifier não encontrado", Toast.LENGTH_LONG).show()
                }
            } else {
                Log.e("LoginActivity", "Código de autorização não encontrado na URL")
                Toast.makeText(this, "Erro: Código de autorização não encontrado", Toast.LENGTH_LONG).show()
            }
        }
    }

    private suspend fun obtainTokens(authorizationCode: String, codeVerifier: String) {
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://accounts.spotify.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val spotifyTokenService = retrofit.create(SpotifyTokenService::class.java)
            val call = spotifyTokenService.getAccessToken(
                grantType = "authorization_code",
                code = authorizationCode,
                redirectUri = REDIRECT_URI,
                clientId = CLIENT_ID,
                clientSecret = CLIENT_SECRET
            )

            val response = withContext(Dispatchers.IO) { call.execute() }

            if (response.isSuccessful) {
                val tokens = response.body()
                if (tokens != null) {
                    saveTokens(tokens.accessToken, tokens.refreshToken)
                    navigateToMainActivity()
                } else {
                    Log.e("LoginActivity", "Erro ao obter token de acesso: Resposta nula")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("LoginActivity", "Erro ao obter token de acesso: $errorBody")
                Toast.makeText(this, "Erro: $errorBody", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Log.e("LoginActivity", "Erro ao obter token de acesso: ${e.message}")
            Toast.makeText(this, "Erro: ${e.message}", Toast.LENGTH_LONG).show()
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

