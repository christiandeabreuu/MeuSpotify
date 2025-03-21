package com.example.spotify.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "albums")
data class AlbumDB(
    @PrimaryKey val databaseId: String, // ID único do álbum
    val name: String,
    val artist: String,
    val imageUrl: String?,
    val releaseDate: String,

)
