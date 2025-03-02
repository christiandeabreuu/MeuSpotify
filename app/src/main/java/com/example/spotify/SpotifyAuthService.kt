package com.example.spotify

import com.google.gson.annotations.SerializedName
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

//interface SpotifyAuthService {
//    @FormUrlEncoded
//    @POST("api/token")
//    suspend fun refreshToken(
//        @Header("Authorization") authorization: String,
//        @Field("grant_type") grantType: String,
//        @Field("refresh_token") refreshToken: String
//    ): RefreshTokenResponse
//}
//
//data class RefreshTokenResponse(
//    @SerializedName("access_token") val accessToken: String,
//    @SerializedName("token_type") val tokenType: String,
//    @SerializedName("expires_in") val expiresIn: Int
//)