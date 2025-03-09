package com.example.spotify.ui.createplaylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.spotify.domain.usecase.CreatePlaylistUseCase
import kotlinx.coroutines.Dispatchers

class CreatePlaylistViewModel(
    private val createPlaylistUseCase: CreatePlaylistUseCase
) : ViewModel() {

    fun createPlaylist(accessToken: String, playlistName: String) = liveData(Dispatchers.IO) {
        try {
            if (playlistName.isBlank()) {
                emit(Result.failure(Exception("Por favor, insira um nome para a playlist.")))
                return@liveData
            }
            val result = createPlaylistUseCase.execute(accessToken, playlistName)
            emit(result)
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}

