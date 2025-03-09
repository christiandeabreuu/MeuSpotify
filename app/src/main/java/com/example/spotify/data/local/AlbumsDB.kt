package com.example.spotify.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AlbumsDB(
    @PrimaryKey(autoGenerate = true)
    val databaseId : Int,
    val items: List<Album>
)

@Entity
data class Album(
    @PrimaryKey(autoGenerate = true)
    val databaseId : Int,
    val id: String,
    val name: String,
    val releaseDate: String,
    val images: List<ImageArtist>
)
