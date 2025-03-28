package com.example.spotify.ui.playlist

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.spotify.R
import com.example.spotify.data.local.SpotifyDatabase
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

        window.navigationBarColor = getColor(R.color.black)
        initializeAccessToken()
        if (accessToken.isBlank()) {
            showErrorAndNavigateToLogin("Token de acesso não encontrado.")
            return
        }

        initializeViewModel()
        observerProfile() // Agora centralizado e chamado cedo
        setupUI()
        setupObservers()
        setupCreatePlaylistButton()
    }

    override fun onResume() {
        super.onResume()
        observerProfile() // Garante o carregamento ao retomar a Activity
    }

    private fun initializeAccessToken() {
        accessToken = intent.getStringExtra("ACCESS_TOKEN") ?: ""
        Log.d("PlaylistActivity", "AccessToken recebido: $accessToken")
    }

    private fun initializeViewModel() {
        val dao = SpotifyDatabase.getSpotifyDatabase(applicationContext).spotifyDao()
        val factory = PlaylistViewModelFactory(
            apiService = RetrofitInstance.api,
            dao = dao,
            accessToken = accessToken,
        )
        viewModel = ViewModelProvider(this, factory)[PlaylistViewModel::class.java]
    }

    private fun setupUI() {
        binding.bottomNavigationView.selectedItemId = R.id.navigation_playlists
        setupBottomNavigationView()
        setupRecyclerView()
    }

    private fun setupObservers() {
        viewModel.playlists.observe(this) { result ->
            result.onSuccess { playlists ->
                playlistAdapter.submitList(playlists)
                Log.d("PlaylistActivity", "Playlists carregadas: $playlists")
            }.onFailure { error ->
                Log.e("PlaylistActivity", "Erro ao carregar playlists: ${error.message}")
                Toast.makeText(this, "Erro ao carregar playlists.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observerProfile() {
        viewModel.fetchProfile().observe(this) { userProfile ->
            val profileImageUrl = userProfile?.imageUrl
            if (!profileImageUrl.isNullOrBlank()) {
                binding.playlistsProfileImageView.load(profileImageUrl) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                    placeholder(R.drawable.ic_spotify_full)
                    error(R.drawable.ic_spotify_full_black)
                }
            } else {
                Log.e("PlaylistActivity", "Imagem de perfil vazia ou não encontrada.")
                binding.playlistsProfileImageView.setImageResource(R.drawable.ic_spotify_full)
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
        intent.addFlags(FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }
}
