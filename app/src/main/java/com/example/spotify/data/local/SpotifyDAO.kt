package com.example.spotify.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SpotifyDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopArtists(topArtists: TopArtistsDB)

    @Query("SELECT * FROM TopArtistsDB")
    suspend fun getTopArtists(): TopArtistsDB

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbums(albums: List<AlbumsDB>)

    suspend fun insertUserProfile(userProfile: List<UserProfileDB>)

    suspend fun insertPlaylists(playlists: List<PlaylistsDB>)


}