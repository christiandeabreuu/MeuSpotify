package com.example.spotify.ui.playlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.spotify.R
import com.example.spotify.data.Playlist
import com.example.spotify.data.UserProfile
import com.example.spotify.databinding.ActivityPlaylistBinding
import com.example.spotify.ui.ArtistActivity
import com.example.spotify.ui.LoginActivity
import com.example.spotify.ui.createplaylist.CreatePlaylistActivity
import com.example.spotify.ui.profile.ProfileActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class PlaylistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaylistBinding
    private val viewModel: PlaylistViewModel by viewModels()
    private lateinit var playlistAdapter: PlaylistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeAcessToken()
        setupUI()
        observeViewModel()
        viewModel.fetchUserProfile()
        viewModel.fetchPlaylists()
    }

    private fun initializeAcessToken() {
        viewModel.accessToken = intent.getStringExtra("ACCESS_TOKEN")
        Log.d("PlaylistActivity", "Access Token: ${viewModel.accessToken}")
        if (viewModel.accessToken.isNullOrEmpty()) {
            Log.e("PlaylistActivity", "Access token is null or empty")
            navigateToLogin()
            return
        }
    }

    private fun setupUI() {
        handleWindowInsets()
        setupBottomNavigationView()
        setupRecyclerView()
        setupCreatePlaylistButton()
    }

    private fun observeViewModel() {
        viewModel.userProfile.observe(this, Observer { userProfile ->
            updateProfileUI(userProfile)
        })
        viewModel.playlists.observe(this, Observer { playlists ->
            updatePlaylistsUI(playlists)
        })
        viewModel.error.observe(this, Observer { errorMessage ->
            Log.e("PlaylistActivity", errorMessage)
        })
    }

    private fun setupRecyclerView() {
        binding.playlistsRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupCreatePlaylistButton() {
        binding.buttonToGoCreatePlaylist.setOnClickListener {
            val intent = Intent(this, CreatePlaylistActivity::class.java)
            intent.putExtra("ACCESS_TOKEN", viewModel.accessToken)
            // You may need to pass userId as well
            startActivity(intent)
        }
    }

    private fun handleWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun updateProfileUI(userProfile: UserProfile) {
        binding.playlistsProfileImageView.load(userProfile.images.firstOrNull()?.url) {
            transformations(coil.transform.CircleCropTransformation())
        }
    }

    private fun updatePlaylistsUI(playlists: List<Playlist>?) {
        Log.d("PlaylistActivity", "Updating playlists UI with ${playlists?.size} playlists")
        if (playlists.isNullOrEmpty()) {
            Log.e("PlaylistActivity", "No playlists available")
            binding.noPlaylistsTextView.visibility = View.VISIBLE
            return
        } else {
            binding.noPlaylistsTextView.visibility = View.GONE
        }
        playlistAdapter = PlaylistAdapter(playlists)
        binding.playlistsRecyclerView.adapter = playlistAdapter
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
        intent.putExtra("ACCESS_TOKEN", viewModel.accessToken)  // Passa o token de acesso
        startActivity(intent)
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
