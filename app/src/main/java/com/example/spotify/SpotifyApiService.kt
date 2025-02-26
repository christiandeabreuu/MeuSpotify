package com.example.spotify

import android.content.Intent
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifyApiService {

    fun authenticate(onSuccess: (String) -> Unit, onError: (String) -> Unit)

    @GET("me")
    fun getUserProfile(@Header("Authorization") accessToken: String): Call<UserProfile>

    fun handleResponse(requestCode: Int, resultCode: Int, data: Intent?, onSuccess: (String) -> Unit, onError: (String) -> Unit)
}
