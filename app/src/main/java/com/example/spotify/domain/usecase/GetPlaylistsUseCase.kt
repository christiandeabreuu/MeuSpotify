import com.example.spotify.data.local.PlaylistDB
import com.example.spotify.data.model.Playlist
import com.example.spotify.data.network.SpotifyApiService
import com.example.spotify.data.repository.PlaylistRepository

class GetPlaylistsUseCase(
    private val repository: PlaylistRepository
) {
    // Busca playlists, priorizando a API e fazendo fallback para o banco de dados
    suspend fun getPlaylists(accessToken: String): List<PlaylistDB> {
        // Tenta buscar os dados da API
        val playlistsFromApi = repository.getPlaylistsFromApi(accessToken)

        return if (playlistsFromApi != null) {
            // Se os dados vierem da API, insere-os no banco e retorna
            val playlistsDB = playlistsFromApi.map { it.toPlaylistDB() }
            repository.insertPlaylistsIntoDB(playlistsDB) // Salva no banco
            playlistsFromApi // Retorna os dados convertidos
        } else {
            // Se a API falhar, busca os dados do banco
            repository.getPlaylistsFromDB()
        }
    }

    fun Playlist.toPlaylistDB(): PlaylistDB {
        return PlaylistDB(
            id = this.id,
            name = this.name,
            description = this.description,
            ownerName = this.owner.name, // Mapeia o nome do proprietário
            tracksCount = this.tracksCount,
            imageUrl = this.images.firstOrNull()?.url // Pega a primeira URL de imagem (se existir)
        )
    }

}

