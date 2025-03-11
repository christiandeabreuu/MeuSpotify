package com.example.spotify.domain.usecase

import android.util.Log
import com.example.spotify.data.local.Artist
import com.example.spotify.data.local.ImageArtist
import com.example.spotify.data.local.SpotifyDAO
import com.example.spotify.data.local.TopArtistsDB
import com.example.spotify.data.local.TopArtistsWithArtistsAndImages
import com.example.spotify.data.model.TopArtistsResponse
import com.example.spotify.data.network.SpotifyApiService
import com.example.spotify.data.repository.TopArtistsRepository

class GetTopArtistsUseCase(
    private val spotifyDAO: SpotifyDAO,
    private val apiService: SpotifyApiService,
    private val repository: TopArtistsRepository = TopArtistsRepository(apiService, spotifyDAO),
) {
    suspend fun getFromApi(accessToken: String, offset: Int = 0, timeRange: String = "medium_term"): TopArtistsResponse {
        Log.d("GetTopArtistsUseCase", "Chamada API com: accessToken=Bearer $accessToken, offset=$offset, timeRange=$timeRange")
        val responseApi = repository.getTopArtistsApi(accessToken, offset, timeRange)
        mapToTopArtistsDB(responseApi, timeRange)
        return responseApi
    }

    private suspend fun mapToTopArtistsDB(response: TopArtistsResponse, timeRange: String) {
        val topArtistsDB = TopArtistsDB(
            total = response.total,
            limit = response.limit,
            offset = response.offset,
            href = response.href ?: "",
            next = response.next,
            previous = response.previous,
            timeRange = timeRange
        )
        val topArtistsId = repository.insertTopArtistsDB(topArtistsDB).toInt()

        val artistsDB = response.items.map { artist ->
            Artist(
                id = artist.id,
                name = artist.name,
                popularity = artist.popularity,
                topArtistsId = topArtistsId
            )
        }
        val artistsIds = repository.insertArtists(artistsDB)

        val imageArtistsDB = response.items.flatMapIndexed { index, artist ->
            artist.images.map { image ->
                ImageArtist(
                    url = image.url,
                    artistId = artistsIds.get(index).toInt()
                )
            }
        }
        repository.insertImageArtists(imageArtistsDB)
    }

//    suspend fun getFromDBWithOffsetAndLimit(limit: Int, offset: Int, timeRange: String = "medium_term"): TopArtistsWithArtistsAndImages {
//        Log.d("GetTopArtistsUseCase", "getFromDBWithOffsetAndLimit() chamado com limit: $limit, offset: $offset, timeRange: $timeRange")
//        return repository.getTopArtistsDBWithOffsetAndLimit(limit, offset, timeRange)
//    }
}