package com.example.spotify.data.repository

import android.util.Log
import com.example.spotify.data.local.SpotifyDAO
import com.example.spotify.data.local.TopArtistsDB
import com.example.spotify.data.model.TopArtistsResponse
import com.example.spotify.data.network.SpotifyApiService

class TopArtistsRepository(private val apiService: SpotifyApiService, private val spotifyDAO: SpotifyDAO){
    suspend fun getTopArtistsApi(accessToken: String, offset: Int = 0): TopArtistsResponse {
        Log.d(
            "GetTopArtistsUseCase",
            "Chamada API com: accessToken=Bearer $accessToken, offset=$offset"
        )
        return apiService.getTopArtists(
            accessToken = "Bearer $accessToken",
            limit = 20,
            timeRange = "medium_term",
            offset = offset
        )
    }

    suspend fun insertTopArtistsDB(topArtists: TopArtistsDB) {
        spotifyDAO.insertTopArtists(topArtists)
    }

    suspend fun getTopArtistsDB(): TopArtistsDB {
        return spotifyDAO.getTopArtists()
    }
}