package com.example.spotify

import android.content.Intent
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifyApiService {
    // Endpoint para obter o perfil do usuário
    @GET("v1/me")
    suspend fun getUserProfile(@Header("Authorization") token: String): UserProfile

    // Endpoint para obter os top artists
    @GET("v1/me/top/artists")
    suspend fun getTopArtists(
        @Header("Authorization") authorization: String,
        @Query("time_range") timeRange: String = "medium_term", // ou "short_term", "long_term"
        @Query("limit") limit: Int = 10 // número de artistas para retornar
    ): Response<TopArtistsResponse>
}

