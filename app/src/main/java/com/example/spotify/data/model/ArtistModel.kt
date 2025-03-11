package com.example.spotify.data.model

import com.google.gson.annotations.SerializedName

class ArtistModel (
    val id: String,
    val name: String,
    val popularity: Int,
    val images: List<ImageArtist>
)