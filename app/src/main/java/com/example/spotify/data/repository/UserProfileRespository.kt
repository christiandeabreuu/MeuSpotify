package com.example.spotify.data.repository


import android.util.Log
import com.example.spotify.data.local.SpotifyDAO
import com.example.spotify.data.local.UserProfileDB
import com.example.spotify.data.model.UserProfile
import com.example.spotify.data.network.SpotifyApiService
import retrofit2.HttpException
import java.io.IOException

class UserProfileRepository(
    private val apiService: SpotifyApiService,
    private val spotifyDAO: SpotifyDAO
) {
    suspend fun getUserProfileFromApi(accessToken: String): UserProfile? {
        return try {
            apiService.getUserProfile("Bearer $accessToken")
        } catch (e: IOException) {
            Log.e("UserProfileRepository", "Erro de rede: ${e.message}")
            null
        } catch (e: HttpException) {
            Log.e("UserProfileRepository", "Erro HTTP: código ${e.code()}, mensagem: ${e.message()}")
            null
        } catch (e: Exception) {
            Log.e("UserProfileRepository", "Erro desconhecido: ${e.message}")
            null
        }
    }

    suspend fun insertUserProfile(userProfile: UserProfileDB) {
        if (spotifyDAO.getUserProfile() != userProfile) {
            spotifyDAO.insertUserProfile(userProfile)
            Log.d("UserProfileRepository", "Perfil inserido no banco: $userProfile")
        } else {
            Log.d("UserProfileRepository", "Perfil já existe no banco, nenhuma inserção feita.")
        }
    }

    suspend fun getUserProfileFromDB(): UserProfileDB? {
        val userProfile = spotifyDAO.getUserProfile()
        if (userProfile == null) {
            Log.w("UserProfileRepository", "Nenhum perfil encontrado no banco local.")
        } else {
            Log.d("UserProfileRepository", "Perfil recuperado do banco: $userProfile")
        }
        return userProfile
    }
}

