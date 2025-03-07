package com.example.spotify.ui.createplaylist

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.spotify.R
import com.example.spotify.databinding.ActivityCreatePlaylistBinding

class CreatePlaylistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePlaylistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleWindowInsets()
        closeButton()
        createButton()
    }

    private fun handleWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun createButton() {
        binding.createButton.setOnClickListener {
            val playlistName = binding.playlistNameEditText.text.toString()
            if (playlistName.isNotEmpty()) {
                createPlaylist(playlistName)
            } else {
                Toast.makeText(this, "Por favor, insira um nome para a playlist.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun closeButton() {
        binding.closeButton.setOnClickListener {
            finish()
        }
    }

    private fun createPlaylist(playlistName: String) {
        // Aqui você pode adicionar a lógica para criar a playlist
        Toast.makeText(this, "Playlist '$playlistName' criada com sucesso!", Toast.LENGTH_SHORT).show()
        // Fechar a atividade após a criação da playlist
        finish()
    }
}
