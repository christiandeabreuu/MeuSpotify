package com.example.spotify.ui.playlist

import GetPlaylistsUseCase
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            try {
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
                _playlists.postValue(Result.success(playlists))
            } catch (e: Exception) {
                Log.e("PlaylistViewModel", "Erro ao carregar playlists: ${e.message}")
                _playlists.postValue(Result.failure(e))
            }
        }
    }
}


