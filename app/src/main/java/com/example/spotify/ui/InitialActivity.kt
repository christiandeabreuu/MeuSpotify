package com.example.spotify.ui


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.example.spotify.R
import com.example.spotify.ui.main.MainActivity


class InitialActivity : AppCompatActivity(R.layout.activity_initial) {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)

        // Verificar se o token de acesso está salvo
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
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

