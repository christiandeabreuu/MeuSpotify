package com.example.spotify.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.data.Playlist
import com.example.spotify.data.RetrofitInstance
import com.example.spotify.data.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistViewModel(private val accessToken: String) : ViewModel() {

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
            try {
                val userProfile = RetrofitInstance.api.getUserProfile("Bearer $accessToken")
                withContext(Dispatchers.Main) {
                    _userProfile.value = userProfile
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _error.value = "Error fetching user profile: ${e.message}"
                }
            }
        }
    }

    private fun fetchPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val playlistsResponse = RetrofitInstance.api.getUserPlaylists("Bearer $accessToken")
                withContext(Dispatchers.Main) {
                    _playlists.value = playlistsResponse.items
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _error.value = "Error fetching playlists: ${e.message}"
                }
            }
        }
    }
}
