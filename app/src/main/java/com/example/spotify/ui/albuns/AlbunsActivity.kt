package com.example.spotify.ui.albuns

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.spotify.R
import com.example.spotify.databinding.ActivityAlbunsBinding
import com.example.spotify.ui.AlbumsViewModelFactory
import com.example.spotify.ui.ArtistActivity
import com.example.spotify.ui.playlist.PlaylistActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AlbumsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlbunsBinding
    private val viewModel: AlbumsViewModel by viewModels { AlbumsViewModelFactory(this) }
    private lateinit var albumsAdapter: AlbumsAdapter
    private lateinit var accessToken: String
    private lateinit var artistId: String
    private lateinit var artistName: String
    private lateinit var imageUrl: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbunsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getIntentData()
        setupViews()
        observeViewModel()
        backbutton()
        setupBottomNavigationView()
    }

    private fun getIntentData() {
        accessToken = intent.getStringExtra("ACCESS_TOKEN") ?: return
        artistId = intent.getStringExtra("ARTIST_ID") ?: return
        artistName = intent.getStringExtra("ARTIST") ?: return
        imageUrl = intent.getStringExtra("IMAGE_URL") ?: return
    }

    private fun setupViews() {
        binding.albumTitleTextView.text = artistName
        binding.albumPostImageView.load(imageUrl)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.albumsRecyclerView.layoutManager = LinearLayoutManager(this)
        albumsAdapter = AlbumsAdapter(listOf())
        binding.albumsRecyclerView.adapter = albumsAdapter
    }

    private fun observeViewModel() {
        viewModel.getAlbums(accessToken, artistId).observe(this, { albums ->
            albums?.let {
                albumsAdapter.updateData(it)
            }
        })
    }

    private fun backbutton() {
        binding.backButton.setOnClickListener {
            finish()
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
                    CoroutineScope(Dispatchers.Main).launch {
                        navigateToActivity(PlaylistActivity::class.java)
                    }
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
        intent.putExtra("ACCESS_TOKEN", accessToken)  // Passar o token de acesso
        startActivity(intent)
    }
}
