package com.example.spotify.data.repository


import android.util.Log
import com.example.spotify.data.local.SpotifyDAO
import com.example.spotify.data.local.UserProfileDB
import com.example.spotify.data.model.UserProfile
import com.example.spotify.data.network.SpotifyApiService

class UserProfileRepository(
    private val apiService: SpotifyApiService, private val spotifyDAO: SpotifyDAO
) {
    suspend fun getUserProfileFromApi(accessToken: String): UserProfile? {
        return try {
            val userProfile = apiService.getUserProfile("Bearer $accessToken")
            userProfile
        } catch (e: Exception) {
            null
        }
    }

    suspend fun insertUserProfile(userProfile: UserProfileDB) {
        spotifyDAO.insertUserProfile(userProfile)
        Log.d("UserProfileRepository", "Perfil inserido no banco: $userProfile")
    }

    suspend fun getUserProfileFromDB(): UserProfileDB? {
        val userProfile = spotifyDAO.getUserProfile()
        Log.d("UserProfileRepository", "Perfil recuperado do banco: $userProfile")
        return userProfile
    }
}
