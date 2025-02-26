package com.example.spotify.data
//
//import retrofit2.Retrofit
//
//import retrofit2.converter.gson.GsonConverterFactory
//
//object RetrofitClient {
//
//    private const val BASE_URL = "https://api.spotify.com/"
//
//    private val retrofit: Retrofit by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//    val apiService: SpotifyApiService by lazy {
//        retrofit.create(SpotifyApiService::class.java)
//    }
//}