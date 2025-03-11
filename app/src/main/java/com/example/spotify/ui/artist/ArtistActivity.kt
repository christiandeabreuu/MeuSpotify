package com.example.spotify.ui.artist

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.spotify.R
import com.example.spotify.data.local.Artist
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtistBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        handleWindowInsets()
        observeArtistsPagingData()

        setupRecyclerView()
        setupBottomNavigationView()

        loadUserData()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val uri: Uri? = intent.data
        Log.d("ArtistActivity", "onNewIntent chamado com URI: $uri")

        if (uri != null && uri.toString().startsWith(Constants.REDIRECT_URI)) {
            // Processa o callback do Spotify e troca pelo token
            val code = uri.getQueryParameter("code")
            Log.d("ArtistActivity", "Código de autorização recebido: $code")
            if (code != null) {
                // Chama o ViewModel ou UseCase para trocar o código por tokens
                viewModel.exchangeCodeForTokens(code, Constants.REDIRECT_URI)
            } else {
                Log.e("ArtistActivity", "Código de autorização ausente no URI")
            }
        }
    }

    private fun goToAlbum(artist: com.example.spotify.data.model.Artist) {
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
        lifecycleScope.launch {
            Log.d("ArtistActivity", "Token passado ao ViewModel: $accessToken")
            viewModel.getArtistsPagingData(accessToken).collectLatest { pagingData ->
                artistAdapter.submitData(pagingData)
            }
        }
    }

    private fun setupRecyclerView() {
        Log.d("ArtistActivity", "Token enviado para o Adapter: $accessToken")
        binding.artistasRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.artistasRecyclerView.adapter = artistAdapter
    }

    private fun loadUserData() {
        viewModel.loadTokens().observe(this) { result ->
            result.onSuccess { tokens ->
                val (accessToken, refreshToken) = tokens
                this.accessToken = accessToken
                Log.d("ArtistActivity", "Token carregado: $accessToken")
                loadProfileData(accessToken, refreshToken)
                observeArtistsPagingData() // Chame após carregar o token
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


    private fun handleWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupBottomNavigationView() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_artistas -> true
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
        intent.addFlags(FLAG_ACTIVITY_SINGLE_TOP)
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
                placeholder(R.drawable.ic_spotify_full)
                error(R.drawable.ic_spotify_full_black)
            }
        }
    }
}