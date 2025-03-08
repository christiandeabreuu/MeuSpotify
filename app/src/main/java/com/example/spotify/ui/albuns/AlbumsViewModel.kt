package com.example.spotify.ui.albuns


import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.spotify.domain.usecase.GetArtistAlbumsUseCase
import kotlinx.coroutines.Dispatchers

class AlbumsViewModel(private val getArtistAlbumsUseCase: GetArtistAlbumsUseCase) : ViewModel() {

    fun getAlbums(accessToken: String, artistId: String) = liveData(Dispatchers.IO) {
        try {
            val albums = getArtistAlbumsUseCase.execute(accessToken, artistId)
            emit(Result.success(albums))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}




