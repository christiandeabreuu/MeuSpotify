package com.example.spotify.domain.usecase

import com.example.spotify.data.local.SpotifyDAO
import com.example.spotify.data.local.UserProfileDB
import com.example.spotify.data.model.UserProfile
import com.example.spotify.data.network.SpotifyApiService

class GetUserProfileUseCase(
    private val apiService: SpotifyApiService,
    private val spotifyDAO: SpotifyDAO
) {
    suspend fun execute(accessToken: String): UserProfile? {
        return try {
            val userProfile = apiService.getUserProfile("Bearer $accessToken")

            // Salvar no banco para uso offline
            userProfile?.let {
                val userProfileDB = UserProfileDB(
                    id = it.id,
                    name = it.displayName,
                    imageUrl = it.images.firstOrNull()?.url
                )
                spotifyDAO.insertUserProfile(userProfileDB)
            }

            userProfile
        } catch (e: Exception) {
            null
        }
    }
}


