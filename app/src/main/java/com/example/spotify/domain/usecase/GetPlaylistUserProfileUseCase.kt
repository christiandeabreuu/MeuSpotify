package com.example.spotify.domain.usecase

import com.example.spotify.data.model.UserProfile
import com.example.spotify.data.network.SpotifyApiService

class GetPlaylistUserProfileUseCase(private val apiService: SpotifyApiService) {
    suspend fun execute(accessToken: String): UserProfile? {
        return try {
            apiService.getUserProfile("Bearer $accessToken")
        } catch (e: Exception) {
            null
        }
    }
}
