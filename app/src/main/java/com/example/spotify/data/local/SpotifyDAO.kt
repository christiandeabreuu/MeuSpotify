package com.example.spotify.data.local

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface SpotifyDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopArtistsDB(topArtistsDB: TopArtistsDB): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE) // Não sobrescreve se já existir
    suspend fun insertArtists(artists: List<Artist>): List<Long>

    @Query("SELECT * FROM artist WHERE id = :artistId")
    suspend fun getArtistByApiId(artistId: String): Artist?

    @Query("SELECT COUNT(*) FROM image_artist WHERE url = :imageUrl AND artistId = :artistId")
    fun getImageCountForArtist(imageUrl: String, artistId: Int): Int



    @Query("UPDATE artist SET name = :name, popularity = :popularity WHERE id = :artistId")
    suspend fun updateArtist(artistId: String, name: String, popularity: Int)



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImageArtists(imageArtists: List<ImageArtist>)


    @Query("SELECT * FROM image_artist WHERE artistId = :artistId")
    suspend fun getImagesForArtist(artistId: Int): List<ImageArtist>

    @Query("SELECT * FROM artist WHERE databaseId = :artistId")
    suspend fun getArtistById(artistId: Int): Artist?

    @Transaction
    @Query("SELECT * FROM top_artists WHERE timeRange = :timeRange LIMIT :limit OFFSET :offset")
    suspend fun getTopArtistsWithOffsetAndLimit(
        limit: Int,
        offset: Int,
        timeRange: String
    ): TopArtistsWithArtistsAndImages


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(userProfile: UserProfileDB): Long

    @Query("SELECT * FROM user_profile LIMIT 1")
    suspend fun getUserProfile(): UserProfileDB?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylists(playlists: List<PlaylistDB>)

    @Query("SELECT * FROM playlist")
    suspend fun getPlaylists():List<PlaylistDB>

}