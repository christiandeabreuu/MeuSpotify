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
    private val repository: PlaylistRepository = PlaylistRepository(spotifyDAO ,apiService)
) {
    suspend fun getFromApi(accessToken: String): List<Playlist> {
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
                imageUrl = playlist.images?.firstOrNull()?.url ?: "" // Garante string não-nula
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
            id = this.id, // ID único da playlist
            name = this.name, // Nome da playlist
            description = this.description, // Descrição da playlist
            owner = Owner(id = "", name = this.ownerName), // Cria o objeto Owner com o nome do proprietário
            tracksCount = this.tracksCount, // Quantidade de músicas na playlist
            images = if (this.imageUrl.isNullOrBlank()) {
                emptyList() // Retorna uma lista vazia se não houver URL de imagem
            } else {
                listOf(Image(url = this.imageUrl)) // Cria uma lista com uma única imagem
            }
        )
    }


}
