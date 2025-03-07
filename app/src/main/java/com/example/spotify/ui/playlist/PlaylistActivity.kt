package com.example.spotify.ui.playlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.spotify.R
import com.example.spotify.data.Playlist
import com.example.spotify.data.PlaylistsResponse
import com.example.spotify.data.RetrofitInstance
import com.example.spotify.data.UserProfile
import com.example.spotify.databinding.ActivityPlaylistBinding
import com.example.spotify.ui.ArtistActivity
import com.example.spotify.ui.LoginActivity
import com.example.spotify.ui.createplaylist.CreatePlaylistActivity
import com.example.spotify.ui.profile.ProfileActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaylistBinding
    private var accessToken: String? = null
    private lateinit var playlistAdapter: PlaylistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleWindowInsets()

        accessToken = intent.getStringExtra("ACCESS_TOKEN")
        Log.d("PlaylistActivity", "Access Token: $accessToken")
        if (accessToken.isNullOrEmpty()) {
            Log.e("PlaylistActivity", "Access token is null or empty")
            navigateToLogin()
            return
        }

        fetchUserProfile()
        fetchPlaylists()
        setupBottomNavigationView()

        binding.playlistsRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun handleWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun fetchUserProfile() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userProfile = RetrofitInstance.api.getUserProfile("Bearer $accessToken")
                withContext(Dispatchers.Main) {
                    updateProfileUI(userProfile)
                }
            } catch (e: Exception) {
                Log.e("PlaylistActivity", "Error fetching user profile", e)
            }
        }
    }

    private fun fetchPlaylists() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("PlaylistActivity", "Fetching playlists...")
                val playlistsResponse = RetrofitInstance.api.getUserPlaylists("Bearer $accessToken")
                Log.d("PlaylistActivity", "Playlists fetched: ${playlistsResponse.items.size}")
                withContext(Dispatchers.Main) {
                    updatePlaylistsUI(playlistsResponse.items)
                }
            } catch (e: Exception) {
                Log.e("PlaylistActivity", "Error fetching playlists", e)
            }
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


        binding.buttonToGoCreatePlaylist.setOnClickListener {
            val intent = Intent(this, CreatePlaylistActivity::class.java)
            startActivity(intent)
        }
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
}
