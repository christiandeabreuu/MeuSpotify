import com.example.spotify.data.model.Playlist
import com.example.spotify.data.network.SpotifyApiService

class GetUserPlaylistsUseCase(private val apiService: SpotifyApiService) {
    suspend fun execute(accessToken: String): List<Playlist>? {
        return try {
            val response = apiService.getUserPlaylists("Bearer $accessToken")
            response.items
        } catch (e: Exception) {
            null
        }
    }
}
