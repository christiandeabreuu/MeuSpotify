package com.example.spotify.ui.playlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.spotify.R
import com.example.spotify.data.local.SpotifyDatabase
import com.example.spotify.data.model.UserProfile
import com.example.spotify.data.network.RetrofitInstance
import com.example.spotify.databinding.ActivityPlaylistBinding
import com.example.spotify.ui.artist.ArtistActivity
import com.example.spotify.ui.createplaylist.CreatePlaylistActivity
import com.example.spotify.ui.login.LoginActivity
import com.example.spotify.ui.profile.ProfileActivity

class PlaylistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaylistBinding
    private lateinit var viewModel: PlaylistViewModel
    private lateinit var accessToken: String
    private lateinit var playlistAdapter: PlaylistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeAccessToken()

        if (accessToken.isBlank()) {
            showErrorAndNavigateToLogin("Token de acesso não encontrado.")
            return
        }

        initializeViewModel()
        setupUI()
        setupObservers()
        setupCreatePlaylistButton()
    }

    private fun initializeAccessToken() {
        accessToken = intent.getStringExtra("ACCESS_TOKEN") ?: ""
        Log.d("PlaylistActivity", "AccessToken recebido: $accessToken")
    }

    private fun initializeViewModel() {
        // Ajustando para passar o SpotifyDAO no PlaylistViewModelFactory
        val dao = SpotifyDatabase.getSpotifyDatabase(applicationContext).spotifyDao()
        val factory = PlaylistViewModelFactory(RetrofitInstance.api, dao, accessToken)
        viewModel = ViewModelProvider(this, factory)[PlaylistViewModel::class.java]
    }

    private fun setupUI() {
        setupBottomNavigationView()
        setupRecyclerView()
    }

    private fun setupObservers() {
        viewModel.userProfile.observe(this) { result ->
            result.onSuccess { userProfile ->
                updateProfileUI(userProfile)
            }.onFailure { error ->
                Log.e("PlaylistActivity", "Erro ao carregar perfil do usuário: ${error.message}")
                Toast.makeText(this, "Erro ao carregar o perfil.", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.playlists.observe(this) { result ->
            result.onSuccess { playlists ->
                playlistAdapter.submitList(playlists)
            }.onFailure { error ->
                Log.e("PlaylistActivity", "Erro ao carregar playlists: ${error.message}")
                Toast.makeText(this, "Erro ao carregar playlists.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        playlistAdapter = PlaylistAdapter()
        binding.playlistsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.playlistsRecyclerView.adapter = playlistAdapter
    }

    private fun setupBottomNavigationView() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_artistas -> {
                    navigateToActivity(ArtistActivity::class.java)
                    true
                }

                R.id.navigation_playlists -> true
                R.id.navigation_profile -> {
                    navigateToActivity(ProfileActivity::class.java)
                    true
                }

                else -> false
            }
        }
        binding.bottomNavigationView.selectedItemId = R.id.navigation_playlists
    }

    private fun setupCreatePlaylistButton() {
        binding.buttonToGoCreatePlaylist.setOnClickListener {
            if (accessToken.isBlank()) {
                showErrorAndNavigateToLogin("Token vazio ao tentar criar uma playlist.")
            } else {
                val intent = Intent(this, CreatePlaylistActivity::class.java)
                intent.putExtra("ACCESS_TOKEN", accessToken)
                startActivity(intent)
            }
        }
    }

    private fun showErrorAndNavigateToLogin(message: String) {
        Log.e("PlaylistActivity", message)
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        navigateToLogin()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        intent.putExtra("ACCESS_TOKEN", accessToken)
        startActivity(intent)
    }

    private fun updateProfileUI(userProfile: UserProfile) {
        binding.playlistsProfileImageView.load(userProfile.images.firstOrNull()?.url) {
            transformations(coil.transform.CircleCropTransformation())
        }
    }
}

