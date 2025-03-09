package com.example.spotify.domain.usecase

import android.util.Log
import com.example.spotify.data.model.UserProfile
import com.example.spotify.data.network.SpotifyApiService

class GetProfileUserUseCase(private val apiService: SpotifyApiService) {
    suspend fun execute(accessToken: String): UserProfile? {
        Log.d("GetProfileUserUseCase", "execute() chamado com accessToken: $accessToken")
        return try {
            val userProfile = apiService.getUserProfile("Bearer $accessToken")
            Log.d("GetProfileUserUseCase", "Perfil do usuário obtido com sucesso: $userProfile")
            userProfile
        } catch (e: Exception) {
            Log.e("GetProfileUserUseCase", "Erro ao buscar perfil do usuário: ${e.message}", e)
            null
        }
    }
}
