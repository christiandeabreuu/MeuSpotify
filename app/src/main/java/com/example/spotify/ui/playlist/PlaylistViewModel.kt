package com.example.spotify.ui.playlist

import GetPlaylistsUseCase
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.spotify.data.local.SpotifyDAO
import com.example.spotify.data.local.UserProfileDB
import com.example.spotify.data.model.Playlist
import com.example.spotify.domain.usecase.GetUserProfilePlaylistUseCase
import kotlinx.coroutines.Dispatchers

class PlaylistViewModel(
    private val getUserProfilePlaylistUseCase: GetUserProfilePlaylistUseCase,
    private val getPlaylistsUseCase: GetPlaylistsUseCase,
    private val spotifyDAO: SpotifyDAO,
    private val accessToken: String
) : ViewModel() {

    private val _playlists = liveData(Dispatchers.IO) {
        try {
            emit(Result.success(getPlaylistsUseCase.getFromDBOrApi(accessToken)))
        } catch (e: Exception) {
            Log.e("PlaylistViewModel", "Erro ao carregar playlists: ${e.message}")
            emit(Result.failure(e))
        }
    }
    val playlists: LiveData<Result<List<Playlist>>> get() = _playlists

    fun fetchProfile() = liveData(Dispatchers.IO) {
        // Primeiro busca no banco
        val cachedProfile = spotifyDAO.getUserProfile()
        if (cachedProfile != null) {
            Log.d("PlaylistViewModel", "Carregando perfil do banco: ${cachedProfile.name}")
            emit(cachedProfile)
        } else {
            // Caso não tenha no banco, busca da API
            try {
                val profile = getUserProfilePlaylistUseCase.getUserProfile(accessToken)
                profile?.let {
                    val userProfileDB = UserProfileDB(
                        databaseId = 0,
                        id = it.id,
                        name = it.displayName,
                        imageUrl = it.images.firstOrNull()?.url
                    )
                    spotifyDAO.insertUserProfile(userProfileDB)
                    Log.d("PlaylistViewModel", "Perfil salvo no banco: ${userProfileDB.name}")
                    emit(userProfileDB)
                }
            } catch (e: Exception) {
                Log.e("PlaylistViewModel", "Erro ao carregar perfil: ${e.message}")
            }
        }
    }
}
