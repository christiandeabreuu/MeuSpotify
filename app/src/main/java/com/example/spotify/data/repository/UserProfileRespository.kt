package com.example.spotify.data.repository


import android.util.Log
import com.example.spotify.data.local.SpotifyDAO
import com.example.spotify.data.local.UserProfileDB
import com.example.spotify.data.model.UserProfile
import com.example.spotify.data.network.SpotifyApiService

class UserProfileRepository(
    private val apiService: SpotifyApiService, private val spotifyDAO: SpotifyDAO
) {
    // Busca o perfil da API
    suspend fun getUserProfileFromApi(accessToken: String): UserProfile? {
        return try {
            Log.d("UserProfileRepository", "Solicitando perfil com token: Bearer $accessToken")
            val userProfile = apiService.getUserProfile("Bearer $accessToken")
            Log.d("UserProfileRepository", "Resposta recebida: $userProfile")
            userProfile
        } catch (e: Exception) {
            Log.e("UserProfileRepository", "Erro na requisição do perfil: ${e.message}")
            null
        }
    }

    // Insere o perfil no banco local
    suspend fun insertUserProfile(userProfile: UserProfileDB) {
        spotifyDAO.insertUserProfile(userProfile)
        Log.d("UserProfileRepository", "Perfil inserido no banco: $userProfile")
    }

    // Recupera o perfil do banco local
    suspend fun getUserProfileFromDB(): UserProfileDB? {
        val userProfile = spotifyDAO.getUserProfile()
        Log.d("UserProfileRepository", "Perfil recuperado do banco: $userProfile")
        return userProfile
    }
}
