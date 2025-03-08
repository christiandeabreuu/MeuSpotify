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
import com.example.spotify.data.model.UserProfile
import com.example.spotify.data.network.RetrofitInstance
import com.example.spotify.databinding.ActivityPlaylistBinding
import com.example.spotify.ui.artist.ArtistActivity
import com.example.spotify.ui.login.LoginActivity
import com.example.spotify.ui.createplaylist.CreatePlaylistActivity
import com.example.spotify.ui.profile.ProfileActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

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

        // Checa se o token está vazio ou inválido
        if (accessToken.isEmpty()) {
            Log.e("PlaylistActivity", "Access token is null or empty")
            Toast.makeText(this, "Erro: Token de acesso não encontrado.", Toast.LENGTH_LONG).show()
            navigateToLogin()
            return
        }

        initializeViewModel()
        setupUI()
        setupObservers()
        goToCreatePlaylist()
    }

    private fun initializeAccessToken() {
        // Recupera o token da Intent
        accessToken = intent.getStringExtra("ACCESS_TOKEN") ?: ""
        Log.d("PlaylistActivity", "AccessToken recebido na Intent: $accessToken")
    }

    private fun initializeViewModel() {
        val factory = PlaylistViewModelFactory(RetrofitInstance.api, accessToken)
        viewModel = ViewModelProvider(this, factory)[PlaylistViewModel::class.java]
    }

    private fun goToCreatePlaylist() {
        binding.buttonToGoCreatePlaylist.setOnClickListener {
            if (accessToken.isBlank()) {
                Log.e("PlaylistActivity", "Token vazio ao navegar para a tela de criar playlist")
                Toast.makeText(this, "Erro: Token de acesso não encontrado.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val intent = Intent(this, CreatePlaylistActivity::class.java)
            intent.putExtra("ACCESS_TOKEN", accessToken) // Passa o token para a próxima Activity
            startActivity(intent)
        }
    }

    private fun setupUI() {
        handleWindowInsets()
        setupBottomNavigationView()
        setupRecyclerView()
    }

    private fun setupObservers() {
        viewModel.userProfile.observe(this) { userProfile ->
            updateProfileUI(userProfile)
        }
        viewModel.error.observe(this) { errorMessage ->
            Log.e("PlaylistActivity", errorMessage)
        }
        viewModel.playlists.observe(this) { playlists ->
            playlistAdapter.submitList(playlists)
        }
    }

    private fun setupRecyclerView() {
        playlistAdapter = PlaylistAdapter()
        binding.playlistsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.playlistsRecyclerView.adapter = playlistAdapter
    }

    private fun handleWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupBottomNavigationView() {
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_artistas -> {
                    navigateToActivity(ArtistActivity::class.java)
                    true
                }
                R.id.navigation_playlists -> {
                    true
                }
                R.id.navigation_profile -> {
                    navigateToActivity(ProfileActivity::class.java)
                    true
                }
                else -> false
            }
        }

        bottomNavigationView.selectedItemId = R.id.navigation_playlists
    }

    private fun navigateToActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        intent.putExtra("ACCESS_TOKEN", accessToken) // Inclui o token em todas as navegações
        Log.d("PlaylistActivity", "Navegando para ${activityClass.simpleName} com AccessToken: $accessToken")
        startActivity(intent)
    }

    private fun navigateToLogin() {
        Log.d("PlaylistActivity", "Navegando para LoginActivity devido ao token inválido")
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun updateProfileUI(userProfile: UserProfile) {
        binding.playlistsProfileImageView.load(userProfile.images.firstOrNull()?.url) {
            transformations(coil.transform.CircleCropTransformation())
        }
    }
}
