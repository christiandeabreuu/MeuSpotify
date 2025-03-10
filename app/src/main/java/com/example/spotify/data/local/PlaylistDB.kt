package com.example.spotify.data.local


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist")
data class PlaylistDB(
    @PrimaryKey(autoGenerate = true)
    val databaseId: Int = 0,
    val name: String, // Nome da playlist
    val description: String?, // Descrição da playlist
    val ownerName: String, // Nome do proprietário da playlist
    val tracksCount: Int, // Quantidade de músicas
    val imageUrl: String? // URL da imagem da playlist
)