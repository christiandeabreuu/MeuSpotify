package com.example.spotify.ui.albuns


import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.spotify.data.network.RetrofitInstance
import com.example.spotify.data.network.SpotifyApiService
import com.example.spotify.domain.usecase.GetArtistAlbumsUseCase
import kotlinx.coroutines.Dispatchers
import retrofit2.awaitResponse

class AlbumsViewModel(private val getArtistAlbumsUseCase: GetArtistAlbumsUseCase) : ViewModel() {

    fun getAlbums(accessToken: String, artistId: String) = liveData(Dispatchers.IO) {
        val albums = getArtistAlbumsUseCase.execute(accessToken, artistId)
        emit(albums)
    }
}



