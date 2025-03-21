package com.example.spotify.ui.albuns


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.spotify.data.local.AlbumDB
import com.example.spotify.data.local.SpotifyDAO
import com.example.spotify.domain.usecase.GetArtistAlbumsUseCase
import kotlinx.coroutines.Dispatchers

class AlbumsViewModel(
    private val getArtistAlbumsUseCase: GetArtistAlbumsUseCase,
    private val spotifyDAO: SpotifyDAO
) : ViewModel() {

    fun fetchAlbums(accessToken: String, artistId: String) = liveData(Dispatchers.IO) {
        // Primeiro busca do banco
        val cachedAlbums = spotifyDAO.getAlbums()
        if (cachedAlbums.isNotEmpty()) {
            emit(Result.success(cachedAlbums)) // Emite os álbuns do banco primeiro
        } else {
            // Se o banco estiver vazio, busca da API
            try {
                val albumsFromApi = getArtistAlbumsUseCase.execute(accessToken, artistId)
                val albumsToSave = albumsFromApi?.map {
                    AlbumDB(
                        databaseId = it.id,
                        name = it.name,
                        artist = it.name,
                        imageUrl = it.images.firstOrNull()?.url,
                        releaseDate = it.releaseDate,

                    )
                }
                if (!albumsToSave.isNullOrEmpty()) {
                    spotifyDAO.insertAlbums(albumsToSave) // Salva no banco
                    emit(Result.success(albumsToSave))
                }
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }
    }

}





