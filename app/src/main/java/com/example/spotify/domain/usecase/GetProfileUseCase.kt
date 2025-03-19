package com.example.spotify.domain.usecase

import android.util.Log
import com.example.spotify.data.local.SpotifyDAO
import com.example.spotify.data.local.UserProfileDB
import com.example.spotify.data.model.Image
import com.example.spotify.data.model.UserProfile
import com.example.spotify.data.network.SpotifyApiService
import com.example.spotify.data.repository.UserProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class GetProfileUserUseCase(
    private val spotifyDAO: SpotifyDAO,
    private val apiService: SpotifyApiService,
    private val repository: UserProfileRepository = UserProfileRepository(apiService, spotifyDAO),
) {
    private var cachedProfile: UserProfile? = null

    suspend fun getUserProfileFromApi(accessToken: String): UserProfile? {
        if (cachedProfile != null) return cachedProfile

        return try {
            val responseApi = repository.getUserProfileFromApi(accessToken)
            if (responseApi != null) {
                mapToUserProfileDB(responseApi)
                cachedProfile = responseApi
            }
            responseApi
        } catch (e: Exception) {
            cachedProfile ?: repository.getUserProfileFromDB()?.let { mapToUserProfile(it) }
        }
    }


    private suspend fun mapToUserProfileDB(response: UserProfile) {
        val existingProfile = repository.getUserProfileFromDB()
        if (existingProfile == null || existingProfile.id != response.id) {
            val userProfileDB = UserProfileDB(
                id = response.id,
                name = response.displayName,
                imageUrl = response.images?.firstOrNull()?.url
            )
            Log.d("GetProfileUserUseCase", "Salvando perfil no banco local: $userProfileDB")
            repository.insertUserProfile(userProfileDB)
        }
    }

    private suspend fun mapToUserProfile(userProfileDB: UserProfileDB): UserProfile = withContext(Dispatchers.IO) {
        UserProfile(
            id = userProfileDB.id,
            displayName = userProfileDB.name,
            images = listOf(Image(url = userProfileDB.imageUrl ?: ""))
        )
    }
}

