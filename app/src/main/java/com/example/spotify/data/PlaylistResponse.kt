package com.example.spotify.data


data class PlaylistsResponse(
    val items: List<Playlist>
)

data class Playlist(
    val name: String,
    val images: List<ImagePlaylist>
)

data class ImagePlaylist(
    val url: String
)
