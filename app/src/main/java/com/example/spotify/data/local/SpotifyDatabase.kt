package com.example.spotify.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        TopArtistsDB::class,
        Artist::class,
        ImageArtist::class,
        UserProfileDB::class,
        PlaylistDB::class,
        AlbumDB::class
    ],
    version = 5
)
abstract class SpotifyDatabase : RoomDatabase() {

    abstract fun spotifyDao(): SpotifyDAO

    companion object {
        @Volatile
        private var INSTANCE: SpotifyDatabase? = null

        fun getSpotifyDatabase(context: Context): SpotifyDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SpotifyDatabase::class.java,
                    "spotify_db"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}