package com.example.spotify.data.model


import com.google.gson.annotations.SerializedName

data class AlbumsResponse(
    @SerializedName("items") val items: List<Album>
)

data class Album(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("images") val images: List<ImageArtist>
)
