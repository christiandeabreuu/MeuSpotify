package com.example.spotify.ui.albuns


import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.spotify.data.RetrofitInstance
import com.example.spotify.data.SpotifyApiService
import kotlinx.coroutines.Dispatchers
import retrofit2.awaitResponse

class AlbumsViewModel(private val spotifyApiService: SpotifyApiService = RetrofitInstance.api) : ViewModel() {

    fun getAlbums(accessToken: String, artistId: String) = liveData(Dispatchers.IO) {
        try {
            val response = spotifyApiService.getArtistAlbums("Bearer $accessToken", artistId).awaitResponse()
            if (response.isSuccessful) {
                emit(response.body()?.items)
            } else {
                emit(null)
            }
        } catch (e: Exception) {
            emit(null)
        }
    }
}


