package com.example.spotify.data.local


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.spotify.data.model.Image
import com.example.spotify.data.model.Owner
import com.example.spotify.data.model.Playlist

@Entity(tableName = "playlist")
data class PlaylistDB(
    @PrimaryKey(autoGenerate = true)
    val databaseId: Int = 0,
    val id: String, // ID da playlist
    val name: String, // Nome da playlist
    val description: String?, // Descrição da playlist
    val ownerName: String, // Nome do proprietário da playlist
    val tracksCount: Int, // Quantidade de músicas
    val imageUrl: String? // URL da imagem da playlist
)

fun PlaylistDB.toPlaylist(): Playlist {
    return Playlist(
        id = this.id,
        name = this.name,
        description = this.description,
        owner = Owner(id = "", name = this.ownerName),
        tracksCount = this.tracksCount,
        images = listOf(Image(url = this.imageUrl ?: ""))
    )
}
