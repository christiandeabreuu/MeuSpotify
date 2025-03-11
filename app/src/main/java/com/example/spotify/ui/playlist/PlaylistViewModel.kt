package com.example.spotify.ui.playlist

import GetPlaylistsUseCase
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.data.local.PlaylistDB
import com.example.spotify.data.model.Image
import com.example.spotify.data.model.Owner
import com.example.spotify.data.model.Playlist
import com.example.spotify.data.model.UserProfile
import com.example.spotify.domain.usecase.GetUserProfilePlaylistUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val getUserProfilePlaylistUseCase: GetUserProfilePlaylistUseCase,
    private val getPlaylistsUseCase: GetPlaylistsUseCase,
    private val accessToken: String
) : ViewModel() {

    private val _userProfile = MutableLiveData<Result<UserProfile>>()
    val userProfile: LiveData<Result<UserProfile>> get() = _userProfile

    private val _playlists = MutableLiveData<Result<List<Playlist>>>()
    val playlists: LiveData<Result<List<Playlist>>> get() = _playlists

    init {
        fetchUserProfile()
        fetchPlaylists()
    }

    private fun fetchUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            try { // essa funcao busca o usuario
                val profile = getUserProfilePlaylistUseCase.getUserProfile(accessToken)
                _userProfile.postValue(Result.success(profile) as Result<UserProfile>?)
            } catch (e: Exception) {
                _userProfile.postValue(Result.failure(e))
            }
        }
    }

    private fun fetchPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val playlists = getPlaylistsUseCase.getFromDBOrApi(accessToken)
                _playlists.postValue(Result.success(playlists)) // Atualiza o LiveData com os dados
            } catch (e: Exception) {
                Log.e("PlaylistViewModel", "Erro ao carregar playlists: ${e.message}")
                _playlists.postValue(Result.failure(e))
            }
        }
    }


    fun PlaylistDB.toPlaylist(): Playlist {
        return Playlist(
            id = this.id, // ID único da playlist
            name = this.name, // Nome da playlist
            description = this.description, // Descrição da playlist
            owner = Owner(id = "", name = this.ownerName), // Cria o objeto Owner usando o nome do proprietário
            tracksCount = this.tracksCount, // Quantidade de músicas
            images = if (this.imageUrl.isNullOrBlank()) {
                emptyList() // Retorna uma lista vazia se imageUrl for nulo ou vazio
            } else {
                listOf(Image(url = this.imageUrl)) // Cria uma lista com uma única imagem
            }
        )
    }




}


