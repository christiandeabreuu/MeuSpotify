package com.example.spotify.data.local

import androidx.room.Embedded
import androidx.room.Relation

data class TopArtistsWithArtistsAndImages(
    @Embedded val topArtists: TopArtistsDB,
    @Relation(
        entity = Artist::class,
        parentColumn = "databaseId",
        entityColumn = "topArtistsId"
    )
    val artists: List<ArtistWithImages>
)

data class ArtistWithImages(
    @Embedded val artist: Artist,
    @Relation(
        parentColumn = "databaseId",
        entityColumn = "artistId"
    )
    val images: List<ImageArtist>
)




