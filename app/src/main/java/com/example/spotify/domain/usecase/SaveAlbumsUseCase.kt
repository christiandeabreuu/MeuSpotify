package com.example.spotify.domain.usecase

import com.example.spotify.data.local.AlbumDB
import com.example.spotify.data.local.SpotifyDAO

class SaveAlbumsUseCase(private val dao: SpotifyDAO) {
    suspend fun save(albums: List<AlbumDB>) {
        dao.insertAlbums(albums)
    }
}
