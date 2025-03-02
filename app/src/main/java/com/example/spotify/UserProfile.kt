package com.example.spotify

data class UserProfile(
    val displayName: String,
    val email: String,
    val id: String
)

data class TopArtistsResponse(
    val items: List<Artist>
)

data class Artist(
    val name: String,
    val id: String,

)

data class Image(
    val url: String,
    val height: Int?,
    val width: Int?
)
