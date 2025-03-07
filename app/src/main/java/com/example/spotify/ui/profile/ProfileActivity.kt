package com.example.spotify.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.load
import com.example.spotify.R
import com.example.spotify.data.model.UserProfile
import com.example.spotify.databinding.ActivityProfileBinding
import com.example.spotify.ui.ArtistActivity
import com.example.spotify.ui.playlist.PlaylistActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var accessToken: String? = null

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(accessToken ?: "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleWindowInsets()
        getAccessToken()

        if (accessToken.isNullOrEmpty()) {
            return
        }

        setupObservers()
        setupUI()
    }

    private fun getAccessToken() {
        accessToken = intent.getStringExtra("ACCESS_TOKEN")
        if (accessToken.isNullOrEmpty()) {
            Log.e("ProfileActivity", "Access token is null or empty")
            return
        }
    }

    private fun handleWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupObservers() {
        viewModel.userProfile.observe(this, { userProfile ->
            updateProfileUI(userProfile)
        })
        viewModel.error.observe(this, { errorMessage ->
            Log.e("ProfileActivity", errorMessage)
        })
    }

    private fun setupUI() {
        closeButton()
        setupBottomNavigationView()
    }

    private fun closeButton() {
        binding.buttonClose.setOnClickListener {
            finishAffinity()
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
                    navigateToActivity(PlaylistActivity::class.java)
                    true
                }
                R.id.navigation_profile -> {
                    true
                }
                else -> false
            }
        }
        bottomNavigationView.selectedItemId = R.id.navigation_profile
    }

    private fun navigateToActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        intent.putExtra("ACCESS_TOKEN", accessToken)  // Passa o token de acesso
        startActivity(intent)
    }

    private fun updateProfileUI(userProfile: UserProfile) {
        binding.profileTextView.text = userProfile.displayName
        userProfile.images.firstOrNull()?.let { image ->
            binding.profileImageView.load(image.url) {
                transformations(coil.transform.CircleCropTransformation())
            }
        }
    }
}
