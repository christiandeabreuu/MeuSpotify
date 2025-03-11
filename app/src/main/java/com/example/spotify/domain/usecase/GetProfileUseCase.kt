package com.example.spotify.domain.usecase

import android.util.Log
import com.example.spotify.data.local.SpotifyDAO
import com.example.spotify.data.local.UserProfileDB
import com.example.spotify.data.model.Image
import com.example.spotify.data.model.UserProfile
import com.example.spotify.data.network.SpotifyApiService
import com.example.spotify.data.repository.UserProfileRepository

class GetProfileUserUseCase(
    private val spotifyDAO: SpotifyDAO,
    private val apiService: SpotifyApiService,
    private val repository: UserProfileRepository = UserProfileRepository(apiService, spotifyDAO),
) {
    suspend fun getUserProfileFromApi(accessToken: String): UserProfile? {
        return try {
            val responseApi = repository.getUserProfileFromApi(accessToken)
            if (responseApi != null) {
                mapToUserProfileDB(responseApi)
                responseApi
            } else {
                throw Exception("Resposta da API nula")
            }
        } catch (e: Exception) {
            val userProfileDB = repository.getUserProfileFromDB()
            userProfileDB?.let { mapToUserProfile(it) }
        }
    }

    private suspend fun mapToUserProfileDB(response: UserProfile) {
        val userProfileDB = UserProfileDB(
            id = response.id,
            name = response.displayName,
            imageUrl = response.images?.firstOrNull()?.url
        )
        Log.d("GetUserProfileUseCase", "Salvando perfil no banco local: $userProfileDB")
        repository.insertUserProfile(userProfileDB)
    }

    private fun mapToUserProfile(userProfileDB: UserProfileDB): UserProfile {
        return UserProfile(
            id = userProfileDB.id,
            displayName = userProfileDB.name,
            images = listOf(Image(url = userProfileDB.imageUrl ?: "")) // Garante que a lista de imagens é criada
        )
    }
}

