package com.example.spotify.domain.usecase

import com.example.spotify.data.model.UserProfile
import com.example.spotify.data.network.SpotifyApiService

class GetUserProfilePlaylistUseCase(private val apiService: SpotifyApiService) {
    suspend fun getUserProfile(accessToken: String): UserProfile? {
        return try { //usuario
            apiService.getUserProfile("Bearer $accessToken")
        } catch (e: Exception) {
            null
        }
    }
}
