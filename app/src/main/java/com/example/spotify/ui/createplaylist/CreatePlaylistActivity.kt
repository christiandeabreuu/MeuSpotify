package com.example.spotify.ui.createplaylist

import PlaylistRepository
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.spotify.data.network.RetrofitInstance
import com.example.spotify.databinding.ActivityCreatePlaylistBinding

class CreatePlaylistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePlaylistBinding
    private lateinit var accessToken: String

    private val viewModel: CreatePlaylistViewModel by viewModels {
        CreatePlaylistViewModelFactory(PlaylistRepository(RetrofitInstance.api))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        accessToken = intent.getStringExtra("ACCESS_TOKEN") ?: ""

        // Validação do token
        if (accessToken.isBlank()) {
            showError("Token de acesso não encontrado.")
            return
        }

        handleWindowInsets()
        setupCreateButton()
        setupCloseButton()
    }

    private fun handleWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupCreateButton() {
        binding.createButton.setOnClickListener {
            val playlistName = binding.playlistNameEditText.text.toString()

            // Logs para depuração
            Log.d("CreatePlaylistActivity", "PlaylistName: $playlistName")

            viewModel.createPlaylist(accessToken, playlistName).observe(this) { result ->
                result.onSuccess { message ->
                    showSuccess(message)
                }.onFailure { exception ->
                    showError(exception.message ?: "Erro desconhecido.")
                }
            }
        }
    }

    private fun setupCloseButton() {
        binding.closeButton.setOnClickListener {
            finish()
        }
    }

    private fun showSuccess(message: String) {
        Log.d("CreatePlaylistActivity", "Success: $message")
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun showError(message: String) {
        Log.e("CreatePlaylistActivity", "Error: $message")
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        finish()
    }
}
