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

    private val _userProfile = MutableLiveData<UserProfile>()
    val userProfile: LiveData<UserProfile> get() = _userProfile

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> get() = _playlists

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    init {
        fetchUserProfile()
        fetchPlaylists()
    }

    private fun fetchUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            val profile = getPlaylistUserProfileUseCase.execute(accessToken)
            withContext(Dispatchers.Main) {
                if (profile != null) {
                    _userProfile.value = profile
                } else {
                    _error.value = "Error fetching user profile"
                }
            }
        }
    }

    private fun fetchPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            val playlists = getUserPlaylistsUseCase.execute(accessToken)
            withContext(Dispatchers.Main) {
                if (playlists != null) {
                    _playlists.value = playlists
                } else {
                    _error.value = "Error fetching playlists"
                }
            }
        }
    }
}

