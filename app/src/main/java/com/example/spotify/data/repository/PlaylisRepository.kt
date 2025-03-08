import android.util.Log
import com.example.spotify.data.model.CreatePlaylistRequest
import com.example.spotify.data.network.SpotifyApiService

class PlaylistRepository(private val apiService: SpotifyApiService) {

    suspend fun createPlaylist(accessToken: String, playlistName: String): String {
        // Adicionando log para verificar a URL e outros dados da requisição
        Log.d("PlaylistRepository", "URL sendo usada: https://api.spotify.com/v1/me/playlists")

        val requestBody = CreatePlaylistRequest(
            name = playlistName,
            public = true // Define se a playlist será pública ou privada
        )

        Log.d("PlaylistRepository", "AccessToken: Bearer $accessToken")
        Log.d("PlaylistRepository", "RequestBody: $requestBody")

        val response = apiService.createPlaylist("Bearer $accessToken", requestBody)

        if (response.isSuccessful) {
            Log.d("PlaylistRepository", "Playlist criada com sucesso!")
            return "Playlist '${playlistName}' criada com sucesso!"
        } else {
            val errorBody = response.errorBody()?.string()
            Log.e("PlaylistRepository", "Erro ao criar playlist: $errorBody")
            throw Exception("Erro ao criar playlist: $errorBody")
        }
    }
}

