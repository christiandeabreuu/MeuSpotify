package com.example.spotify.ui.playlist

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.spotify.R
import com.example.spotify.databinding.ActivityPlaylistBinding
import com.example.spotify.ui.ArtistActivity
import com.example.spotify.ui.profile.ProfileActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaylistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupBottomNavigationView()
    }

    private fun setupBottomNavigationView() {
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_artistas -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        navigateToActivity(ArtistActivity::class.java)
                    }
                    true
                }
                R.id.navigation_playlists -> {
                    // Evita recriar a atividade de playlists
                    true
                }
                R.id.navigation_profile -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        navigateToActivity(ProfileActivity::class.java)
                    }
                    true
                }
                else -> false
            }
        }

        // Define o item selecionado de acordo com a tela atual
        bottomNavigationView.selectedItemId = R.id.navigation_playlists
    }

    private suspend fun navigateToActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }
}
