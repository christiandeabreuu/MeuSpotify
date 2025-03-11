import com.example.spotify.data.model.CreatePlaylistRequest
import com.example.spotify.data.network.SpotifyApiService

class CreatePlaylistRepository(private val apiService: SpotifyApiService) {

    suspend fun createPlaylist(accessToken: String, playlistName: String): String {
        val requestBody = CreatePlaylistRequest(
            name = playlistName, public = true // Define se a playlist será pública ou privada
        )
        val response = apiService.createPlaylist("Bearer $accessToken", requestBody)
        if (response.isSuccessful) {
            return "Playlist '${playlistName}' criada com sucesso!"
        } else {
            val errorBody = response.errorBody()?.string()
            throw Exception("Erro ao criar playlist: $errorBody")
        }
    }
}

