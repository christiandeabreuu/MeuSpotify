package com.example.spotify.data.network

import com.example.spotify.data.model.AlbumsResponse
import com.example.spotify.data.model.CreatePlaylistRequest
import com.example.spotify.data.model.PlaylistsResponse
import com.example.spotify.data.model.TopArtistsResponse
import com.example.spotify.data.model.UserProfile
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
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

    @GET("me/playlists")
    suspend fun getUserPlaylists(@Header("Authorization") authorization: String): PlaylistsResponse

    @POST("me/playlists") // Removido o "v1/"
    suspend fun createPlaylist(
        @Header("Authorization") accessToken: String,
        @Body requestBody: CreatePlaylistRequest
    ): Response<Unit>


}


