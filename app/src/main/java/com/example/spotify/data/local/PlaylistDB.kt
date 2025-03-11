package com.example.spotify.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist")
data class PlaylistDB(
    @PrimaryKey(autoGenerate = true)
    val databaseId: Int = 0,
    val id: String,
    val name: String,
    val description: String?,
    val ownerName: String,
    val tracksCount: Int,
    val imageUrl: String?
)