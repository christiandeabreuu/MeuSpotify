package com.example.spotify.domain.usecase

import com.example.spotify.data.local.Artist
import com.example.spotify.data.local.ImageArtist
import com.example.spotify.data.local.SpotifyDAO
import com.example.spotify.data.local.TopArtistsDB
import com.example.spotify.data.model.TopArtistsResponse
import com.example.spotify.data.network.SpotifyApiService
import com.example.spotify.data.repository.TopArtistsRepository

class GetTopArtistsUseCase(
    private val spotifyDAO: SpotifyDAO,
    private val apiService: SpotifyApiService,
    private val repository: TopArtistsRepository = TopArtistsRepository(apiService, spotifyDAO),
) {
    suspend fun getFromApi(accessToken: String, offset: Int = 0): TopArtistsDB {

        val responseApi = repository.getTopArtistsApi(accessToken, offset)

        repository.insertTopArtistsDB(mapToTopArtistsDB(responseApi))
        return repository.getTopArtistsDB()
    }

    fun mapToTopArtistsDB(response: TopArtistsResponse): TopArtistsDB {
        val artistsDB = response.items.map { artist ->
            Artist(databaseId = 0,
                id = artist.id,
                name = artist.name,
                popularity = artist.popularity,
                images = artist.images.map { image ->
                    ImageArtist(
                        databaseId = 0,
                        url = image.url
                    )
                })
        }

        return TopArtistsDB(
            databaseId = 0,
            items = artistsDB,
            total = response.total,
            limit = response.limit,
            offset = response.offset,
            href = response.href,
            next = response.next,
            previous = response.previous
        )
    }
}

