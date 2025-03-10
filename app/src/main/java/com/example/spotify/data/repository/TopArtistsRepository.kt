package com.example.spotify.data.repository

import android.util.Log
import com.example.spotify.data.local.Artist
import com.example.spotify.data.local.ImageArtist
import com.example.spotify.data.local.SpotifyDAO
import com.example.spotify.data.local.TopArtistsDB
import com.example.spotify.data.local.TopArtistsWithArtistsAndImages
import com.example.spotify.data.model.TopArtistsResponse
import com.example.spotify.data.network.SpotifyApiService

class TopArtistsRepository(private val apiService: SpotifyApiService, private val spotifyDAO: SpotifyDAO) {
    suspend fun getTopArtistsApi(accessToken: String, offset: Int = 0, timeRange: String = "medium_term"): TopArtistsResponse {
        Log.d(
            "TopArtistsRepository",
            "Chamada API com: accessToken=Bearer $accessToken, offset=$offset, timeRange=$timeRange"
        )
        return apiService.getTopArtists(
            accessToken = "Bearer $accessToken",
            limit = 20,
            timeRange = timeRange,
            offset = offset
        )
    }

    suspend fun insertTopArtistsDB(topArtists: TopArtistsDB): Long {
        return spotifyDAO.insertTopArtistsDB(topArtists)
    }

    suspend fun insertArtists(artists: List<Artist>): List<Long> {
        return spotifyDAO.insertArtists(artists)
    }

    suspend fun insertImageArtists(imageArtists: List<ImageArtist>) {
        spotifyDAO.insertImageArtists(imageArtists)
    }

    suspend fun getTopArtistsDBWithOffsetAndLimit(limit: Int, offset: Int, timeRange: String): TopArtistsWithArtistsAndImages {
        Log.d("TopArtistsRepository", "getTopArtistsDBWithOffsetAndLimit() chamado com limit: $limit, offset: $offset, timeRange: $timeRange")
        return spotifyDAO.getTopArtistsWithOffsetAndLimit(limit, offset, timeRange)
    }
}