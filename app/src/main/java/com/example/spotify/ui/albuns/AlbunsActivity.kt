package com.example.spotify.ui.albuns

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.spotify.databinding.ActivityAlbunsBinding
import com.example.spotify.ui.AlbumsViewModelFactory
import com.example.spotify.ui.adapters.AlbumsAdapter



class AlbumsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlbunsBinding
    private val viewModel: AlbumsViewModel by viewModels { AlbumsViewModelFactory(this) }
    private lateinit var albumsAdapter: AlbumsAdapter
    private lateinit var accessToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbunsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        accessToken = intent.getStringExtra("ACCESS_TOKEN") ?: return
        val artistId = intent.getStringExtra("ARTIST_ID") ?: return
        val artistName = intent.getStringExtra("ARTIST") ?: return
        val imageUrl = intent.getStringExtra("IMAGE_URL") ?: return

        binding.albumsRecyclerView.layoutManager = LinearLayoutManager(this)
        albumsAdapter = AlbumsAdapter(listOf())
        binding.albumsRecyclerView.adapter = albumsAdapter

        binding.albumTitleTextView.text = artistName
        binding.albumPostImageView.load(imageUrl)

        binding.backButton.setOnClickListener {
            finish()
        }

        viewModel.getAlbums(accessToken, artistId).observe(this, { albums ->
            albums?.let {
                albumsAdapter.updateData(it)
            }
        })
    }
}
