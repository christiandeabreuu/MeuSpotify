package com.example.spotify.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserProfileDB(
    @PrimaryKey(autoGenerate = true)
    val databaseId : Int,
    val displayName: String,
    val email: String,
    val id: String,
    val images: List<Image>
)

@Entity
data class Image(
    @PrimaryKey(autoGenerate = true)
    val databaseId : Int,
    val url: String
)
