package com.example.spotify.ui.albuns

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.spotify.R
import com.example.spotify.data.network.RetrofitInstance
import com.example.spotify.databinding.ActivityAlbunsBinding
import com.example.spotify.ui.artist.ArtistActivity
import com.example.spotify.ui.playlist.PlaylistActivity

class AlbumsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlbunsBinding
    private lateinit var accessToken: String
    private lateinit var artistId: String
    private lateinit var artistName: String
    private lateinit var imageUrl: String
    private val viewModel: AlbumsViewModel by viewModels {
        AlbumsViewModelFactory(RetrofitInstance.api)
    }
    private lateinit var albumsAdapter: AlbumsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbunsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        artistName = intent.getStringExtra("ARTIST") ?: ""
        imageUrl = intent.getStringExtra("IMAGE_URL") ?: ""

        if (!getIntentData()) return
        setupViews()
        observeViewModel()
        setupBackButton()
        setupBottomNavigationView()
    }

    private fun getIntentData(): Boolean {
        Log.d("AlbumsActivity", "Extras do Intent: ${intent.extras?.keySet()}") // Confirma se os extras existem
        accessToken = intent.getStringExtra("ACCESS_TOKEN") ?: return handleError("Token de acesso não encontrado.")
        artistId = intent.getStringExtra("ARTIST_ID") ?: return handleError("ID do artista não encontrado.")
        Log.d("AlbumsActivity", "getIntentData: ACCESS_TOKEN=$accessToken, ARTIST_ID=$artistId")
        return true
    }

    private fun handleError(message: String): Boolean {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        finish()
        return false
    }

    private fun setupViews() {
        binding.albumTitleTextView.text = artistName
        binding.albumPostImageView.load(imageUrl) {
            transformations(coil.transform.CircleCropTransformation())
            placeholder(R.drawable.ic_launcher_background)
            error(R.drawable.ic_launcher_foreground)
        }
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.albumsRecyclerView.layoutManager = LinearLayoutManager(this)
        albumsAdapter = AlbumsAdapter(listOf())
        binding.albumsRecyclerView.adapter = albumsAdapter
    }

    private fun observeViewModel() {
        viewModel.getAlbums(accessToken, artistId).observe(this) { result ->
            result.onSuccess { albums ->
                if (albums != null) {
                    Log.d("AlbumsActivity", "Álbuns carregados: ${albums.size} encontrados.")
                    if (albums.isNotEmpty()) {
                        albumsAdapter.updateData(albums)
                    } else {
                        Toast.makeText(this, "Nenhum álbum encontrado.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("AlbumsActivity", "Resposta da API retornou nula.")
                }
            }.onFailure { e ->
                Log.e("AlbumsActivity", "Erro ao carregar álbuns: ${e.message}", e)
                Toast.makeText(this, "Erro ao carregar álbuns.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupBackButton() {
        binding.backButton.setOnClickListener {
            finish()
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

                R.id.navigation_profile -> {
                    true
                }

                else -> false
            }
        }
    }

    private fun navigateToActivity(activityClass: Class<*>) {
        Log.d("AlbumsActivity", "Navegando para ${activityClass.simpleName} com ACCESS_TOKEN=$accessToken")
        val intent = Intent(this, activityClass)
        intent.putExtra("ACCESS_TOKEN", accessToken)
        intent.addFlags(FLAG_ACTIVITY_NO_ANIMATION)
        intent.addFlags(FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
    }
}

