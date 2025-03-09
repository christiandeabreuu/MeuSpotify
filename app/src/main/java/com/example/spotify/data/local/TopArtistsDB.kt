package com.example.spotify.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TopArtistsDB(
    @PrimaryKey(autoGenerate = true)
    val databaseId : Int,
    val items: List<Artist>,
    val total: Int,
    val limit: Int,
    val offset: Int,
    val href: String?,
    val next: String?,
    val previous: String?
)

@Entity
data class Artist(
    @PrimaryKey(autoGenerate = true)
    val databaseId : Int,
    val id: String,
    val name: String,
    val popularity: Int,
    val images: List<ImageArtist>
)

@Entity
data class ImageArtist(
    @PrimaryKey(autoGenerate = true)
    val databaseId : Int,
    val url: String
)
