package com.example.spotify.data.model

import com.google.gson.annotations.SerializedName

data class TopArtistsResponse(
    @SerializedName("items") val items: List<Artist>,
    @SerializedName("total") val total: Int,
    @SerializedName("limit") val limit: Int,
    @SerializedName("offset") val offset: Int,
    @SerializedName("href") val href: String,
    @SerializedName("next") val next: String? = null,
    @SerializedName("previous") val previous: String? = null
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