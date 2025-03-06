package com.example.spotify.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.spotify.data.RetrofitInstance
import com.example.spotify.data.SpotifyApiService
import com.example.spotify.auth.SpotifyAuthHelper
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import retrofit2.awaitResponse
import java.io.IOException

class MainViewModel(private val spotifyAuthHelper: SpotifyAuthHelper) : ViewModel() {

    private val spotifyApiService: SpotifyApiService = RetrofitInstance.api

    fun loadTokens(context: Context) = liveData(Dispatchers.IO) {
        val sharedPreferences = context.getSharedPreferences("SpotifyPrefs", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("ACCESS_TOKEN", "") ?: ""
        val refreshToken = sharedPreferences.getString("REFRESH_TOKEN", "") ?: ""
        emit(Pair(accessToken, refreshToken))
    }

    fun getUserProfile(accessToken: String) = liveData(Dispatchers.IO) {
        try {
            val userProfile = spotifyApiService.getUserProfile("Bearer $accessToken")
            emit(userProfile)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                emit(null) // Emitir nulo em caso de erro 401
            } else {
                emit(null)
            }
        } catch (e: IOException) {
            emit(null)
        } catch (e: Exception) {
            emit(null)
        }
    }

    fun refreshToken(refreshToken: String) = liveData(Dispatchers.IO) {
        try {
            val tokens = spotifyAuthHelper.refreshAccessToken(refreshToken)
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
                if (response.code() == 401) {
                    emit(null)
                } else {
                    emit(null)
                }
            }
        } catch (e: IOException) {
            emit(null)
        } catch (e: HttpException) {
            emit(null)
        }
    }
}
