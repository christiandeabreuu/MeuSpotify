package com.example.spotify.ui.createplaylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers

class CreatePlaylistViewModel : ViewModel() {

    fun createPlaylist(playlistName: String) = liveData(Dispatchers.IO) {
        if (playlistName.isNotEmpty()) {
            emit(Result.success("Playlist '$playlistName' criada com sucesso!"))
        } else {
            emit(Result.failure<String>(Exception("Por favor, insira um nome para a playlist.")))
        }
    }
}
