package com.example.spotify.ui.profile

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.spotify.R
import com.example.spotify.data.local.SpotifyDAO
import com.example.spotify.data.local.SpotifyDatabase
import com.example.spotify.data.model.UserProfile
import com.example.spotify.data.network.RetrofitInstance
import com.example.spotify.data.repository.UserProfileRepository
import com.example.spotify.databinding.ActivityProfileBinding
import com.example.spotify.ui.artist.ArtistActivity
import com.example.spotify.ui.login.LoginActivity
import com.example.spotify.ui.playlist.PlaylistActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {
    private lateinit var spotifyDAO: SpotifyDAO
    private lateinit var binding: ActivityProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private var accessToken: String? = null

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.navigationBarColor = getColor(R.color.black)
        getAccessToken()

        if (accessToken.isNullOrEmpty()) {
            navigateToLogin()
            return
        }
        initializedatabase()
        initializeViewModel()
        setupObservers()
        setupUI()
    }

    private fun initializedatabase() {
        val database = SpotifyDatabase.getSpotifyDatabase(applicationContext)
        spotifyDAO = database.spotifyDao()
    }

    private fun getAccessToken() {
        accessToken = intent.getStringExtra("ACCESS_TOKEN")
        if (accessToken.isNullOrEmpty()) {
            Log.e("ProfileActivity", "Access token is null or empty")
        } else {
            Log.d("ProfileActivity", "Access token recebido: $accessToken")
        }
    }

    private fun initializeViewModel() {
        val apiService = RetrofitInstance.api
        val repository = UserProfileRepository(apiService, spotifyDAO)
        val factory = ProfileViewModelFactory(RetrofitInstance.api, spotifyDAO, repository, accessToken!!)
        viewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun setupUI() {
        binding.bottomNavigationView.selectedItemId = R.id.navigation_profile
        setupCloseButton()
        setupBottomNavigationView()
    }

    private fun setupCloseButton() {
        binding.buttonClose.setOnClickListener {
            finishAffinity()
        }
    }

    private fun setupBottomNavigationView() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_artistas -> {
                    navigateToActivity(ArtistActivity::class.java)

                    true
                }
                R.id.navigation_playlists -> {
                    navigateToActivity(PlaylistActivity::class.java)
                    true
                }
                R.id.navigation_profile -> true
                else -> false
            }

        }
    }

    private fun navigateToActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass).apply {
            putExtra("ACCESS_TOKEN", accessToken)
            addFlags(FLAG_ACTIVITY_NO_ANIMATION)
        }
        startActivity(intent)
    }

    private fun setupObservers() {
        viewModel.userProfile.observe(this) { result ->
            result.onSuccess { userProfile ->
                Log.d("ProfileActivity", "Usuário recebido: $userProfile")
                updateProfileUI(userProfile)
            }.onFailure { error ->
                Log.e("ProfileActivity", "Erro ao buscar perfil do usuário: ${error.message}")
            }
        }
    }

    private fun updateProfileUI(userProfile: UserProfile) {
        Log.d("ProfileActivity", "Atualizando UI com o nome: ${userProfile.displayName} e imagem: ${userProfile.images.firstOrNull()?.url}")
        binding.profileTextView.text = userProfile.displayName ?: "Usuário desconhecido"
        userProfile.images.firstOrNull()?.let { image ->
            binding.profileImageView.load(image.url) {
                transformations(coil.transform.CircleCropTransformation())
                placeholder(R.drawable.ic_spotify_full)
                error(R.drawable.ic_spotify_full_black)
            }
        }
    }
}

