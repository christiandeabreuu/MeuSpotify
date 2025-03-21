package com.example.spotify.ui.artist

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.spotify.R
import com.example.spotify.data.local.SpotifyDAO
import com.example.spotify.data.model.Artist
import com.example.spotify.databinding.ActivityArtistBinding
import com.example.spotify.ui.albuns.AlbumsActivity
import com.example.spotify.ui.login.LoginActivity
import com.example.spotify.ui.playlist.PlaylistActivity
import com.example.spotify.ui.profile.ProfileActivity
import com.example.spotify.utils.Constants
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ArtistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArtistBinding
    private val viewModel: ArtistViewModel by viewModels { ArtistViewModelFactory(this) }
    private val artistAdapter: ArtistAdapter by lazy { ArtistAdapter(accessToken) { goToAlbum(it) } }
    private var accessToken: String = ""
    private lateinit var spotifyDAO : SpotifyDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtistBinding.inflate(layoutInflater)
        enableEdgeToEdge()

        setContentView(binding.root)

        window.navigationBarColor = getColor(R.color.black)
        binding.bottomNavigationView.selectedItemId = R.id.navigation_artistas
        observeArtistsPagingData()
        setupRecyclerView()
        observerProfile()
        setupBottomNavigationView()
        loadUserData()

    }



    private fun observerProfile() {
        viewModel.getUserProfileImage().observe(this) { imageUrl ->
            Log.d("ObserverProfile", "URL da imagem carregada: $imageUrl")
            binding.profileImageView.load(imageUrl) {
                crossfade(true)
                transformations(CircleCropTransformation())
                placeholder(R.drawable.ic_spotify_full)
                error(R.drawable.ic_spotify_full_black)
            }

        }
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        binding.bottomNavigationView.selectedItemId = R.id.navigation_artistas

        val uri: Uri? = intent.data

        if (uri != null && uri.toString().startsWith(Constants.REDIRECT_URI)) {
            val code = uri.getQueryParameter("code")
            if (code != null) {
                viewModel.exchangeCodeForTokens(code, Constants.REDIRECT_URI)
            }
        }
    }

    private fun goToAlbum(artist: Artist) {
        val intent = Intent(this, AlbumsActivity::class.java).apply {
            putExtra("ARTIST_ID", artist.id)
            putExtra("ACCESS_TOKEN", accessToken)
            putExtra("ARTIST", artist.name)
            putExtra("IMAGE_URL", artist.images.firstOrNull()?.url)
        }
        Log.d("ArtistAdapter", "Token ao iniciar a Activity: $accessToken")
        startActivity(intent)
    }

    private fun observeArtistsPagingData() {
        if (accessToken.isNotEmpty()) {
            lifecycleScope.launch {
                viewModel.getArtistsPagingData(accessToken).collectLatest { pagingData ->
                    artistAdapter.submitData(pagingData)
                }
            }
        }
    }


    private fun setupRecyclerView() {
        binding.artistasRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.artistasRecyclerView.adapter = artistAdapter
    }

    private fun loadUserData() {
        viewModel.loadTokens().observe(this) { result ->
            result.onSuccess { tokens ->
                val (accessToken, refreshToken) = tokens
                this.accessToken = accessToken
                loadProfileData(accessToken, refreshToken)
                // Só inicialize a observação após garantir que o token está carregado.
                observeArtistsPagingData()
            }.onFailure {
                navigateToLogin()
            }
        }
    }


    private fun loadProfileData(accessToken: String, refreshToken: String) {
        viewModel.getUserProfile(accessToken).observe(this) { result ->
            result.onSuccess { profile ->
                profile?.images?.firstOrNull()?.url?.let { imageProfile(it) }
            }.onFailure {
                refreshAccessToken(refreshToken)
            }
        }
    }

    private fun refreshAccessToken(refreshToken: String) {
        viewModel.refreshToken(refreshToken).observe(this) { result ->
            result.onSuccess { tokens ->
                if (tokens != null) {
                    viewModel.saveAccessToken(tokens.accessToken, tokens.refreshToken)
                }
                if (tokens != null) {
                    loadProfileData(tokens.accessToken, tokens.refreshToken)
                }
            }.onFailure {
                navigateToLogin()
            }
        }
    }

    private fun loadArtistsData() {
        lifecycleScope.launch {
            viewModel.getArtistsPagingData("query").collectLatest { pagingData ->
                artistAdapter.submitData(pagingData)
            }
        }
    }

    private fun setupBottomNavigationView() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_artistas ->
                    true
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
        intent.putExtra("ACCESS_TOKEN", accessToken)
        intent.addFlags(FLAG_ACTIVITY_NO_ANIMATION)
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
            }
        }
    }
}