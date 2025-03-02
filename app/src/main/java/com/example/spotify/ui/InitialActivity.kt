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
            //            // Verificar se o token de acesso está salvo
            val sharedPrefs = getSharedPreferences("SPOTIFY", Context.MODE_PRIVATE)
            val accessToken = sharedPrefs.getString("ACCESS_TOKEN", null)

            if (accessToken != null) {
                // Token existe, ir para MainActivity
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("ACCESS_TOKEN", accessToken)
                startActivity(intent)
                finish()
            } else {
                // Sem token, ir para LoginActivity
                val spotifyAuthHelper = SpotifyAuthHelper(this)
                spotifyAuthHelper.redirectToLogin()

            }
        }
    }
}


