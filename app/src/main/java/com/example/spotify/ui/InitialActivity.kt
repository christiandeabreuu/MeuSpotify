package com.example.spotify.ui


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.example.spotify.R
import com.example.spotify.auth.SpotifyAuthHelper
import com.example.spotify.databinding.ActivityInitialBinding


class InitialActivity : AppCompatActivity(R.layout.activity_initial) {

    private lateinit var binding: ActivityInitialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enterButton()

    }

    private fun enterButton() {
        binding.startButton.setOnClickListener {
            val sharedPrefs = getSharedPreferences("SPOTIFY", Context.MODE_PRIVATE)
            val accessToken = sharedPrefs.getString("ACCESS_TOKEN", null)
            val tokenTimestamp = sharedPrefs.getLong("TOKEN_TIMESTAMP", 0)

            if (accessToken != null && !isTokenExpired(tokenTimestamp)) {
                // Token existe e não expirou, ir para MainActivity
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("ACCESS_TOKEN", accessToken)
                startActivity(intent)
                finish()
            } else {
                // Token expirado ou não existe, ir para LoginActivity
                val spotifyAuthHelper = SpotifyAuthHelper(this)
                spotifyAuthHelper.redirectToLogin()
            }
        }
    }

    private fun isTokenExpired(tokenTimestamp: Long): Boolean {
        val currentTime = System.currentTimeMillis()
        return (currentTime - tokenTimestamp) >= 3600 * 1000 // 1 hora em milissegundos
    }
}


