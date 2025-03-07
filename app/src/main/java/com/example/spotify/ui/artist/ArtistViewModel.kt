package com.example.spotify.ui.artist

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.spotify.data.network.RetrofitInstance
import com.example.spotify.data.network.SpotifyApiService
import com.example.spotify.auth.SpotifyAuthHelper
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import retrofit2.awaitResponse
import java.io.IOException

class ArtistViewModel(private val context: Context) : ViewModel() {

    private val spotifyApiService: SpotifyApiService = RetrofitInstance.api
    private val spotifyAuthHelper: SpotifyAuthHelper = SpotifyAuthHelper(context)

    fun loadTokens() = liveData(Dispatchers.IO) {
        val sharedPreferences = context.getSharedPreferences("SpotifyPrefs", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("ACCESS_TOKEN", "") ?: ""
        val refreshToken = sharedPreferences.getString("REFRESH_TOKEN", "") ?: ""
        emit(Pair(accessToken, refreshToken))
    }

    fun saveAccessToken(accessToken: String, refreshToken: String) {
        val sharedPreferences = context.getSharedPreferences("SpotifyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("ACCESS_TOKEN", accessToken)
        editor.putString("REFRESH_TOKEN", refreshToken)
        editor.apply()
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
