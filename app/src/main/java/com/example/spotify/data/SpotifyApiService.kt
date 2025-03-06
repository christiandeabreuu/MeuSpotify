package com.example.spotify.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotifyApiService {

    @GET("me")
    suspend fun getUserProfile(@Header("Authorization") authorization: String): UserProfile

    @GET("me/top/artists")
    fun getTopArtists(
        @Header("Authorization") accessToken: String,
        @Query("limit") limit: Int = 20,
        @Query("time_range") timeRange: String = "medium_term"
    ): Call<TopArtistsResponse>

    @GET("artists/{id}/albums")
    fun getArtistAlbums(@Header("Authorization") authorization: String, @Path("id") artistId: String): Call<AlbumsResponse>
}


