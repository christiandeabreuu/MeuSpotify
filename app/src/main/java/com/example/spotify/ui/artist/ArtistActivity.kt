package com.example.spotify.ui.artist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.spotify.R
import com.example.spotify.data.network.RetrofitInstance
import com.example.spotify.databinding.ActivityArtistBinding
import com.example.spotify.ui.login.LoginActivity
import com.example.spotify.ui.playlist.PlaylistActivity
import com.example.spotify.ui.profile.ProfileActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class ArtistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArtistBinding
    private val viewModel: ArtistViewModel by viewModels { ArtistViewModelFactory(this) }
    private lateinit var artistAdapter: ArtistAdapter
    private var accessToken: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleWindowInsets()
        bottomNavigationView()

        binding.artistasRecyclerView.layoutManager = LinearLayoutManager(this)
        loadUserData()
    }

    private fun loadUserData() {
        viewModel.loadTokens().observe(this) { tokens ->
            val (accessToken, refreshToken) = tokens
            if (accessToken.isNotEmpty()) {
                this.accessToken = accessToken
                lifecycleScope.launch {
                    viewModel.getUserProfile(accessToken).observe(this@ArtistActivity) { profile ->
                        profile?.let {
                            imageProfile(it.images.firstOrNull()?.url)
                        } ?: run {
                            viewModel.refreshToken(refreshToken).observe(this@ArtistActivity) { newTokens ->
                                newTokens?.let {
                                    viewModel.saveAccessToken(it.accessToken, it.refreshToken)
                                    viewModel.getUserProfile(it.accessToken)
                                } ?: navigateToLogin()
                            }
                        }
                    }

                    viewModel.getTopArtists(accessToken).observe(this@ArtistActivity) { artists ->
                        artists?.let {
                            artistAdapter = ArtistAdapter(it, this@ArtistActivity, accessToken)
                            binding.artistasRecyclerView.adapter = artistAdapter
                        }
                    }
                }
            } else {
                navigateToLogin()
            }
        }
    }

    private fun handleWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun bottomNavigationView() {
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_artistas -> {
                    true
                }
                R.id.navigation_playlists -> {
                    navigateToActivity(PlaylistActivity::class.java)
                    true
                }
                R.id.navigation_profile -> {
                    navigateToActivity(ProfileActivity::class.java)
                    true
                }
                else -> false
            }
        }
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

    private fun imageProfile(imageUrl: String?) {
        imageUrl?.let {
            binding.profileImageView.load(it) {
                transformations(CircleCropTransformation())
                placeholder(R.drawable.ic_launcher_background)
                error(R.drawable.ic_launcher_foreground)
            }
        }
    }
}

