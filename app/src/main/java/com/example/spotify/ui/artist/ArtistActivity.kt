package com.example.spotify.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.spotify.R
import com.example.spotify.databinding.ActivityArtistBinding
import com.example.spotify.ui.artist.ArtistAdapter
import com.example.spotify.ui.artist.ArtistViewModel
import com.example.spotify.ui.artist.ArtistViewModelFactory
import com.example.spotify.ui.playlist.PlaylistActivity
import com.example.spotify.ui.profile.ProfileActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

        bottomNavigationView()

        binding.artistasRecyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.loadTokens(this).observe(this, Observer { tokens ->
            val (accessToken, refreshToken) = tokens
            if (accessToken.isNotEmpty()) {
                this.accessToken = accessToken
                lifecycleScope.launch {
                    viewModel.getUserProfile(accessToken)
                        .observe(this@ArtistActivity, Observer { profile ->
                            profile?.let {
                                imageProfile(it.images.firstOrNull()?.url)
                            } ?: run {
                                viewModel.refreshToken(refreshToken)
                                    .observe(this@ArtistActivity, Observer { newTokens ->
                                        newTokens?.let {
                                            saveAccessToken(it.accessToken, it.refreshToken)
                                            viewModel.getUserProfile(it.accessToken)
                                        } ?: navigateToLogin()
                                    })
                            }
                        })

                    viewModel.getTopArtists(accessToken)
                        .observe(this@ArtistActivity, Observer { artists ->
                            artists?.let {
                                artistAdapter = ArtistAdapter(it, this@ArtistActivity, accessToken)
                                binding.artistasRecyclerView.adapter = artistAdapter
                            }
                        })
                }
            } else {
                navigateToLogin()
            }
        })
    }

    private fun bottomNavigationView() {
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_artistas -> {
                    true
                }

                R.id.navigation_playlists -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        navigateToActivity(PlaylistActivity::class.java)
                    }
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
    }

    private suspend fun navigateToActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        intent.putExtra("ACCESS_TOKEN", accessToken)  // Passa o token de acesso
        startActivity(intent)
    }

    private fun saveAccessToken(accessToken: String, refreshToken: String) {
        val sharedPreferences = getSharedPreferences("SpotifyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("ACCESS_TOKEN", accessToken)
        editor.putString("REFRESH_TOKEN", refreshToken)
        editor.apply()
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
