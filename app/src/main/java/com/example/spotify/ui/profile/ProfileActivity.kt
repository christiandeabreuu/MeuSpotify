package com.example.spotify.ui.profile

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.spotify.R
import com.example.spotify.data.local.SpotifyDAO
import com.example.spotify.data.local.SpotifyDatabase
import com.example.spotify.data.model.UserProfile
import com.example.spotify.data.network.RetrofitInstance
import com.example.spotify.data.network.SpotifyApiService
import com.example.spotify.data.repository.UserProfileRepository
import com.example.spotify.databinding.ActivityProfileBinding
import com.example.spotify.ui.artist.ArtistActivity
import com.example.spotify.ui.login.LoginActivity
import com.example.spotify.ui.playlist.PlaylistActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileActivity : AppCompatActivity() {
    private lateinit var apiService: SpotifyApiService
    private lateinit var spotifyDAO: SpotifyDAO
    private lateinit var repository: UserProfileRepository
    private lateinit var binding: ActivityProfileBinding
    private val viewModel: ProfileViewModel by viewModels { ProfileViewModelFactory(apiService, spotifyDAO, repository) }
    private var accessToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar dependências antes de usá-las
        apiService = RetrofitInstance.api
        spotifyDAO = SpotifyDatabase.getSpotifyDatabase(applicationContext).spotifyDao()
        repository = UserProfileRepository(apiService, spotifyDAO)

        getAccessToken()
        if (accessToken.isNullOrEmpty()) {
            navigateToLogin()
            return
        }

        setupUI()
        observeUserProfile()
    }


    override fun onResume() {
        super.onResume()
        if (!accessToken.isNullOrEmpty()) {
            viewModel.loadUserProfile(accessToken!!)
        }
    }

    private fun getAccessToken() {
        accessToken = intent.getStringExtra("ACCESS_TOKEN")
        Log.d("ProfileActivity", "Access token recebido: $accessToken")
    }

    private fun observeUserProfile() {
        lifecycleScope.launch {
            viewModel.userProfile.collect { result ->
                result?.onSuccess { userProfile ->
                    updateProfileUI(userProfile)
                }?.onFailure { error ->
                    Log.e("ProfileActivity", "Erro ao buscar perfil: ${error.message}")
                }
            }
        }
    }




    private fun updateProfileUI(userProfile: UserProfile) {
        binding.profileTextView.text = userProfile.displayName ?: "Usuário desconhecido"
        userProfile.images.firstOrNull()?.let { image ->
            binding.profileImageView.load(image.url) {
                crossfade(true)
                transformations(coil.transform.CircleCropTransformation())

            }
        }
    }

    private fun setupUI() {
        binding.bottomNavigationView.selectedItemId = R.id.navigation_profile
        setupBottomNavigationView()
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

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}


