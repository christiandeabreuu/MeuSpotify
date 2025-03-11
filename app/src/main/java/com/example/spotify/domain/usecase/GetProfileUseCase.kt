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
    // Função principal que busca os dados
    suspend fun getUserProfileFromApi(accessToken: String): UserProfile? {
        return try {
            // Tenta buscar os dados da API
            Log.d("GetUserProfileUseCase", "Buscando perfil do usuário na API com token: Bearer $accessToken")
            val responseApi = repository.getUserProfileFromApi(accessToken)
            if (responseApi != null) {
                // Salva no banco local e retorna os dados da API
                mapToUserProfileDB(responseApi)
                responseApi
            } else {
                throw Exception("Resposta da API nula")
            }
        } catch (e: Exception) {
            Log.e("GetUserProfileUseCase", "Erro ao buscar dados da API, tentando carregar do banco local: ${e.message}")
            val userProfileDB = repository.getUserProfileFromDB()
            userProfileDB?.let { mapToUserProfile(it) }
        }
    }

    // Mapeia o modelo da API para o banco de dados
    private suspend fun mapToUserProfileDB(response: UserProfile) {
        val userProfileDB = UserProfileDB(
            id = response.id,
            name = response.displayName,
            imageUrl = response.images?.firstOrNull()?.url
        )
        Log.d("GetUserProfileUseCase", "Salvando perfil no banco local: $userProfileDB")
        repository.insertUserProfile(userProfileDB)
    }

    // Converte o modelo do banco de dados (UserProfileDB) para o modelo da API (UserProfile)
    private fun mapToUserProfile(userProfileDB: UserProfileDB): UserProfile {
        return UserProfile(
            id = userProfileDB.id,
            displayName = userProfileDB.name,
            images = listOf(Image(url = userProfileDB.imageUrl ?: "")) // Garante que a lista de imagens é criada
        )
    }

}

