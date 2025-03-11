package com.example.spotify.data.model

import com.google.gson.annotations.SerializedName

data class UserProfile(
    @SerializedName("display_name")
    val displayName: String,
    val id: String,
    val images: List<Image>
)

data class Image(
    val url: String
)
