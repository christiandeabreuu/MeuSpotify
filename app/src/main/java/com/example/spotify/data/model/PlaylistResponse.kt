package com.example.spotify.data.model

import com.google.gson.annotations.SerializedName


data class PlaylistsResponse(
    @SerializedName("items") val items: List<Playlist>
)


data class Playlist(
    val id: String,
    val name: String,
    val description: String?,
    @SerializedName("owner") val owner: Owner,
    val tracksCount: Int,
    val images: List<Image>
)


data class Owner(
    val id: String,
    @SerializedName("display_name") val name: String
)


data class ImagePlaylist(
    val url: String
)

