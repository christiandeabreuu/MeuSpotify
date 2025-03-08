package com.example.spotify.ui.createplaylist

import PlaylistRepository
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.example.spotify.data.network.RetrofitInstance
import com.example.spotify.databinding.ActivityCreatePlaylistBinding

class CreatePlaylistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePlaylistBinding
    private lateinit var accessToken: String

    private val viewModel: CreatePlaylistViewModel by viewModels {
        // Criando o ViewModel com o accessToken atualizado
        CreatePlaylistViewModelFactory(PlaylistRepository(RetrofitInstance.api), accessToken)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recuperando o AccessToken enviado pela Intent
        accessToken = intent.getStringExtra("ACCESS_TOKEN") ?: ""

        // Adicionando log para verificar o token
        Log.d("CreatePlaylistActivity", "AccessToken recebido: $accessToken")

        // Validando se o AccessToken está presente
        if (accessToken.isBlank()) {
            Log.e("CreatePlaylistActivity", "AccessToken está vazio! Finalizando Activity.")
            Toast.makeText(this, "Erro: Token de acesso não encontrado.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

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

            // Validando o nome da playlist antes de enviar
            if (playlistName.isBlank()) {
                Toast.makeText(this, "Por favor, insira um nome para a playlist.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Logs para depuração
            Log.d("CreatePlaylistActivity", "PlaylistName: $playlistName")
            Log.d("CreatePlaylistActivity", "AccessToken: $accessToken")

            // Chamada para criar a playlist
            viewModel.createPlaylist(playlistName).observe(this, Observer { result ->
                result.onSuccess { message ->
                    Log.d("CreatePlaylistActivity", "Success Message: $message")
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    finish() // Fecha a Activity após criar a playlist
                }.onFailure { e ->
                    Log.e("CreatePlaylistActivity", "Erro: ${e.message}")
                    Toast.makeText(this, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
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
