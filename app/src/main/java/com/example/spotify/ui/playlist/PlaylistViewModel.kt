package com.example.spotify.ui.playlist

import GetUserPlaylistsUseCase
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.data.local.toPlaylist
import com.example.spotify.data.model.Playlist
import com.example.spotify.data.model.UserProfile
import com.example.spotify.domain.usecase.GetPlaylistUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val getPlaylistUseCase: GetPlaylistUseCase,
    private val getUserPlaylistsUseCase: GetUserPlaylistsUseCase,
    private val accessToken: String
) : ViewModel() {

    private val _userProfile = MutableLiveData<Result<UserProfile>>()
    val userProfile: LiveData<Result<UserProfile>> get() = _userProfile

    private val _playlists = MutableLiveData<Result<List<Playlist>>>()
    val playlists: LiveData<Result<List<Playlist>>> get() = _playlists

    init {
        fetchPlaylists()
        fetchUserProfilePlaylist()
    }

    private fun fetchPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Primeiro tenta buscar as playlists da API
                Log.d("PlaylistViewModel", "Buscando playlists da API")
                val playlistsFromApi = getPlaylistUseCase.getPlaylistsFromApi(accessToken)
                _playlists.postValue(Result.success(playlistsFromApi.items)) // Posta o resultado da API
            } catch (apiException: Exception) {
                Log.e("PlaylistViewModel", "Erro ao buscar API: ${apiException.message}. Tentando banco de dados...")

                // Caso a API falhe, tenta buscar playlists do banco
                try {
                    val playlistsFromDB = getPlaylistUseCase.getPlaylistsFromDB().map { it.toPlaylist() }
                    _playlists.postValue(Result.success(playlistsFromDB)) // Posta o fallback do banco
                } catch (dbException: Exception) {
                    Log.e("PlaylistViewModel", "Erro ao buscar do banco: ${dbException.message}")
                    _playlists.postValue(Result.failure(dbException)) // Posta falha completa
                }
            }
        }
    }



    private fun fetchUserProfilePlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val playlists = getUserPlaylistsUseCase.execute(accessToken)
                _playlists.postValue(Result.success(playlists) as Result<List<Playlist>>?)
            } catch (e: Exception) {
                _playlists.postValue(Result.failure(e))
            }
        }
    }
}


