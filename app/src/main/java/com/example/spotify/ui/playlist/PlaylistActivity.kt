package com.example.spotify.ui.playlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.spotify.R
import com.example.spotify.data.model.UserProfile
import com.example.spotify.databinding.ActivityPlaylistBinding
import com.example.spotify.ui.ArtistActivity
import com.example.spotify.ui.login.LoginActivity
import com.example.spotify.ui.createplaylist.CreatePlaylistActivity
import com.example.spotify.ui.profile.ProfileActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class PlaylistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaylistBinding
    private lateinit var accessToken: String
    private val viewModel: PlaylistViewModel by viewModels {
        PlaylistViewModelFactory(accessToken)
    }
    private lateinit var playlistAdapter: PlaylistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeAccessToken()
        setupUI()
        setupObservers()
        goToCreatePlaylist()
    }

    private fun initializeAccessToken() {
        accessToken = intent.getStringExtra("ACCESS_TOKEN") ?: ""
        Log.d("PlaylistActivity", "Access Token: $accessToken")
        if (accessToken.isEmpty()) {
            Log.e("PlaylistActivity", "Access token is null or empty")
            navigateToLogin()
        }
    }

    private fun goToCreatePlaylist() {
        binding.buttonToGoCreatePlaylist.setOnClickListener{
            val intent = Intent(this, CreatePlaylistActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupUI() {
        handleWindowInsets()
        setupBottomNavigationView()
        setupRecyclerView()
    }

    private fun setupObservers() {
        viewModel.userProfile.observe(this, Observer { userProfile ->
            updateProfileUI(userProfile)
        })
        viewModel.error.observe(this, Observer { errorMessage ->
            Log.e("PlaylistActivity", errorMessage)
        })
        viewModel.playlists.observe(this, Observer { playlists ->
            playlistAdapter.submitList(playlists)
        })
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
        intent.putExtra("ACCESS_TOKEN", accessToken)  // Passa o token de acesso
        startActivity(intent)
    }

    private fun navigateToLogin() {
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
