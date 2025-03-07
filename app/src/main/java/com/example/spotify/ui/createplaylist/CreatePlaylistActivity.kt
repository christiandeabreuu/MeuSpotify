package com.example.spotify.ui.createplaylist

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.example.spotify.R
import com.example.spotify.databinding.ActivityCreatePlaylistBinding

class CreatePlaylistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePlaylistBinding
    private val viewModel: CreatePlaylistViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleWindowInsets()
        closeButton()
        setupCreateButton()
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
            viewModel.createPlaylist(playlistName).observe(this, Observer { result ->
                result.onSuccess { message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    finish()
                }.onFailure { e ->
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun closeButton() {
        binding.closeButton.setOnClickListener {
            finish()
        }
    }
}
