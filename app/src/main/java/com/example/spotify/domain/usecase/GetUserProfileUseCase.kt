package com.example.spotify.domain.usecase

import android.util.Log
import com.example.spotify.data.model.UserProfile
import com.example.spotify.data.network.SpotifyApiService

class GetUserProfileUseCase(private val apiService: SpotifyApiService) {
    suspend fun execute(accessToken: String): UserProfile? {
        return try {
            Log.d("GetUserProfileUseCase", "Solicitando perfil com token: Bearer $accessToken")
            val userProfile = apiService.getUserProfile("Bearer $accessToken")
            Log.d("GetUserProfileUseCase", "Resposta recebida: $userProfile")
            userProfile
        } catch (e: Exception) {
            Log.e("GetUserProfileUseCase", "Erro na requisição do perfil: ${e.message}")
            null // Retorna nulo em caso de erro
        }
    }
}

