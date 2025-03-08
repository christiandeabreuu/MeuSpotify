package com.example.spotify.ui.playlist

import GetUserPlaylistsUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.data.model.Playlist
import com.example.spotify.data.network.RetrofitInstance
import com.example.spotify.data.model.UserProfile
import com.example.spotify.domain.usecase.GetPlaylistUserProfileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistViewModel(
    private val getPlaylistUserProfileUseCase: GetPlaylistUserProfileUseCase,
    private val getUserPlaylistsUseCase: GetUserPlaylistsUseCase,
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
                val profile = getPlaylistUserProfileUseCase.execute(accessToken)
                _userProfile.postValue(Result.success(profile) as Result<UserProfile>?)
            } catch (e: Exception) {
                _userProfile.postValue(Result.failure(e))
            }
        }
    }

    private fun fetchPlaylists() {
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


