package com.example.spotify.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class PlaylistsDB(
    @PrimaryKey(autoGenerate = true)
    val databaseId : Int,
    val items: List<Playlist>
)

@Entity
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    val databaseId : Int,
    val id: String,
    val name: String,
    val description: String?,
    val owner: Owner,
    val tracksCount: Int,
    val images: List<Image>
)

@Entity
data class Owner(
    @PrimaryKey(autoGenerate = true)
    val databaseId : Int,
    val id: String,
    val name: String
)


data class ImagePlaylist(
    @PrimaryKey(autoGenerate = true)
    val databaseId : Int,
    val url: String
)

