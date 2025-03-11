import android.util.Log
import com.example.spotify.data.local.PlaylistDB
import com.example.spotify.data.local.SpotifyDAO
import com.example.spotify.data.model.Playlist
import com.example.spotify.data.network.SpotifyApiService

class PlaylistRepository(
    private val spotifyDAO: SpotifyDAO,
    private val apiService: SpotifyApiService
) {
    suspend fun getPlaylistsFromApi(accessToken: String): List<Playlist>? {
        return try {
            val response = apiService.getPlaylists("Bearer $accessToken")
            response.items
        } catch (e: Exception) {
            null
        }
    }

    suspend fun insertPlaylistsIntoDB(playlists: List<PlaylistDB>) {
        spotifyDAO.insertPlaylists(playlists)
    }

    suspend fun getPlaylistsFromDB(): List<PlaylistDB> {
        val playlists = spotifyDAO.getPlaylists()
        Log.d("PlaylistRepository", "Playlists recuperadas do banco: $playlists")
        return playlists
    }

}
