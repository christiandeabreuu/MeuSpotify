package com.example.spotify

import android.content.Intent
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifyApiService {

    @GET("me")
    suspend fun getUserProfile(@Header("Authorization") authorization: String): UserProfile

}

//-------------------------------------------
//-------------------------------------------

//fun handleResponse(requestCode: Int, resultCode: Int, data: Intent?, onSuccess: (String) -> Unit, onError: (String) -> Unit)
//fun authenticate(onSuccess: (String) -> Unit, onError: (String) -> Unit)

