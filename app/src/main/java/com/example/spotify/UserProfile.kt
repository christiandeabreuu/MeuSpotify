package com.example.spotify

data class UserProfile(
    val displayName: String,
    val email: String,
    val id: String,
    val images: List<Image>
)

data class Image(
    val url: String
)
