package com.example.spotify.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.spotify.R
import com.example.spotify.databinding.ActivityMainBinding
import com.example.spotify.ui.adapters.ArtistAdapter
import com.example.spotify.ui.main.MainViewModel
import com.example.spotify.ui.main.MainViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels { MainViewModelFactory(this) }
    private lateinit var artistAdapter: ArtistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.artistasRecyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.loadTokens(this).observe(this, Observer { tokens ->
            val (accessToken, refreshToken) = tokens
            if (accessToken.isNotEmpty()) {
                lifecycleScope.launch {
                    viewModel.getUserProfile(accessToken).observe(this@MainActivity, Observer { profile ->
                        profile?.let {
                            imageProfile(it.images.firstOrNull()?.url)
                        } ?: run {
                            viewModel.refreshToken(refreshToken).observe(this@MainActivity, Observer { newTokens ->
                                newTokens?.let {
                                    saveAccessToken(it.accessToken, it.refreshToken)
                                    viewModel.getUserProfile(it.accessToken)
                                } ?: navigateToLogin()
                            })
                        }
                    })

                    viewModel.getTopArtists(accessToken).observe(this@MainActivity, Observer { artists ->
                        artists?.let {
                            artistAdapter = ArtistAdapter(it, this@MainActivity, accessToken)
                            binding.artistasRecyclerView.adapter = artistAdapter
                        }
                    })
                }
            } else {
                navigateToLogin()
            }
        })
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
