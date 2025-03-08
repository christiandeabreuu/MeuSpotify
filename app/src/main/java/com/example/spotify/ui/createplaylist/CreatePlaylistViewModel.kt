package com.example.spotify.ui.createplaylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.spotify.domain.usecase.CreatePlaylistUseCase
import kotlinx.coroutines.Dispatchers

class CreatePlaylistViewModel(
    private val createPlaylistUseCase: CreatePlaylistUseCase,
    private val accessToken: String
) : ViewModel() {

    fun createPlaylist(playlistName: String) = liveData(Dispatchers.IO) {
        if (playlistName.isNotEmpty()) {
            val result = createPlaylistUseCase.execute(accessToken, playlistName)
            emit(result)
        } else {
            emit(Result.failure<String>(Exception("Por favor, insira um nome para a playlist.")))
        }
    }
}
