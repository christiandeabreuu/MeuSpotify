package com.example.spotify.data.model

import com.google.gson.annotations.SerializedName

data class TopArtistsResponse(
    @SerializedName("items") val items: List<Artist>
)

data class Artist(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("popularity") val popularity: Int,
    @SerializedName("images") val images: List<ImageArtist>
)

data class ImageArtist(
    @SerializedName("url") val url: String
)
