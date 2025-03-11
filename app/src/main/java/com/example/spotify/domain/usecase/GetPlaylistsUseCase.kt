import android.util.Log
import com.example.spotify.data.local.PlaylistDB
import com.example.spotify.data.local.SpotifyDAO
import com.example.spotify.data.model.Image
import com.example.spotify.data.model.Owner
import com.example.spotify.data.model.Playlist
import com.example.spotify.data.network.SpotifyApiService

class GetPlaylistsUseCase(
    private val spotifyDAO: SpotifyDAO,
    private val apiService: SpotifyApiService,
    private val repository: PlaylistRepository = PlaylistRepository(spotifyDAO, apiService)
) {
    suspend fun getFromApi(accessToken: String): List<Playlist> {
        Log.d("GetPlaylistsUseCase", "Chamando API com accessToken=Bearer $accessToken")
        val playlistsFromApi = repository.getPlaylistsFromApi(accessToken)
        playlistsFromApi?.let { mapToPlaylistDB(it) }
        return playlistsFromApi ?: emptyList()
    }

    private suspend fun mapToPlaylistDB(playlists: List<Playlist>) {
        val playlistsDB = playlists.map { playlist ->
            PlaylistDB(
                id = playlist.id,
                name = playlist.name,
                description = playlist.description,
                ownerName = playlist.owner.name,
                tracksCount = playlist.tracksCount,
                imageUrl = playlist.images.firstOrNull()?.url ?: ""
            )
        }
        repository.insertPlaylistsIntoDB(playlistsDB)
        Log.d("GetPlaylistsUseCase", "Playlists salvas no banco: ${playlistsDB.size}")
    }

    suspend fun getFromDBOrApi(accessToken: String): List<Playlist> {
        val playlistsFromDB = repository.getPlaylistsFromDB().map { it.toPlaylist() }
        Log.d("GetPlaylistsUseCase", "Playlists carregadas do banco: ${playlistsFromDB.size}")

        return if (playlistsFromDB.isNotEmpty()) {
            playlistsFromDB
        } else {
            Log.d("GetPlaylistsUseCase", "Banco vazio, tentando buscar playlists da API")
            try {
                getFromApi(accessToken)
            } catch (e: Exception) {
                Log.e("GetPlaylistsUseCase", "Erro ao buscar playlists da API: ${e.message}")
                emptyList()
            }
        }
    }

    fun PlaylistDB.toPlaylist(): Playlist {
        return Playlist(
            id = this.id,
            name = this.name,
            description = this.description,
            owner = Owner(
                id = "",
                name = this.ownerName
            ),
            tracksCount = this.tracksCount,
            images = if (this.imageUrl.isNullOrBlank()) {
                emptyList()
            } else {
                listOf(Image(url = this.imageUrl))
            }
        )
    }
}
