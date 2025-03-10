package com.example.spotify.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface SpotifyDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopArtistsDB(topArtistsDB: TopArtistsDB): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtists(artists: List<Artist>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImageArtists(imageArtists: List<ImageArtist>)

    @Transaction
    @Query("SELECT * FROM top_artists WHERE timeRange = :timeRange LIMIT :limit OFFSET :offset")
    suspend fun getTopArtistsWithOffsetAndLimit(limit: Int, offset: Int, timeRange: String): TopArtistsWithArtistsAndImages
}