package com.example.spotify

import com.google.gson.annotations.SerializedName

data class TopArtistsResponse(
    @SerializedName("items") val items: List<Artist>
)

data class Artist(
    @SerializedName("name") val name: String,
    @SerializedName("popularity") val popularity: Int,
    @SerializedName("images") val images: List<ImageArtist>
)

data class ImageArtist(
    @SerializedName("url") val url: String
//    val height: Int,
//    val width: Int
)
