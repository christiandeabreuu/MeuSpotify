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
    suspend fun getTopArtistsWithOffsetAndLimit(
        limit: Int, offset: Int, timeRange: String
    ): TopArtistsWithArtistsAndImages

    @Transaction
    @Query("SELECT * FROM artist WHERE topArtistsId = :topArtistsId LIMIT :limit OFFSET :offset")
    fun getTopArtistsWithImages(topArtistsId: Int, limit: Int, offset: Int): List<ArtistWithImages>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(userProfile: UserProfileDB): Long

    @Query("SELECT * FROM user_profile LIMIT 1")
    suspend fun getUserProfile(): UserProfileDB?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylists(playlists: List<PlaylistDB>)

    @Query("SELECT * FROM playlist")
    suspend fun getPlaylists(): List<PlaylistDB>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbums(albums: List<AlbumDB>)

    @Query("SELECT * FROM albums")
    suspend fun getAlbums(): List<AlbumDB>

    @Query("SELECT * FROM albums WHERE databaseId = :albumId LIMIT 1")
    suspend fun getAlbumById(albumId: String): AlbumDB?


}