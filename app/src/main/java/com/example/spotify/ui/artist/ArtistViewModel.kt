package com.example.spotify.ui.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.spotify.data.network.SpotifyApiService
import com.example.spotify.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import retrofit2.awaitResponse
import java.io.IOException

class ArtistViewModel(
    private val authRepository: AuthRepository,
    private val spotifyApiService: SpotifyApiService
) : ViewModel() {

    fun loadTokens() = liveData(Dispatchers.IO) {
        val tokens = authRepository.loadTokens()
        emit(tokens)
    }

    fun saveAccessToken(accessToken: String, refreshToken: String) {
        authRepository.saveTokens(accessToken, refreshToken)
    }

    fun getUserProfile(accessToken: String) = liveData(Dispatchers.IO) {
        try {
            val userProfile = spotifyApiService.getUserProfile("Bearer $accessToken")
            emit(userProfile)
        } catch (e: HttpException) {
            emit(null) // Emitir nulo em caso de erro 401 ou outros erros
        } catch (e: IOException) {
            emit(null)
        } catch (e: Exception) {
            emit(null)
        }
    }

    fun refreshToken(refreshToken: String) = liveData(Dispatchers.IO) {
        try {
            val tokens = authRepository.refreshAccessToken(refreshToken)
            emit(tokens)
        } catch (e: Exception) {
            emit(null)
        }
    }

    fun getTopArtists(accessToken: String) = liveData(Dispatchers.IO) {
        val call = spotifyApiService.getTopArtists("Bearer $accessToken")
        try {
            val response = call.awaitResponse()
            if (response.isSuccessful) {
                emit(response.body()?.items)
            } else {
                emit(null)
            }
        } catch (e: IOException) {
            emit(null)
        } catch (e: HttpException) {
            emit(null)
        }
    }
}
