package com.example.spotify.ui.createplaylist

import PlaylistRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spotify.domain.usecase.CreatePlaylistUseCase

class CreatePlaylistViewModelFactory(
    private val playlistRepository: PlaylistRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreatePlaylistViewModel::class.java)) {
            val createPlaylistUseCase = CreatePlaylistUseCase(playlistRepository)
            return CreatePlaylistViewModel(createPlaylistUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
