package com.example.spotify.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "top_artists")
data class TopArtistsDB(
    @PrimaryKey(autoGenerate = true)
    val databaseId: Int = 0,
    val total: Int,
    val limit: Int,
    val offset: Int,
    val href: String,
    val next: String?,
    val previous: String?,
    val timeRange: String
)

@Entity(
    tableName = "artist",
    foreignKeys = [ForeignKey(
        entity = TopArtistsDB::class,
        parentColumns = ["databaseId"],
        childColumns = ["topArtistsId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Artist(
    @PrimaryKey(autoGenerate = true)
    val databaseId: Int = 0,
    val id: String,
    val name: String,
    val popularity: Int,
    val topArtistsId: Int
)

@Entity(
    tableName = "image_artist",
    foreignKeys = [ForeignKey(
        entity = Artist::class,
        parentColumns = ["databaseId"],
        childColumns = ["artistId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ImageArtist(
    @PrimaryKey(autoGenerate = true)
    val databaseId: Int = 0,
    val url: String,
    val artistId: Int
)
