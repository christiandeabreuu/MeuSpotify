package com.example.spotify.domain.usecase

import android.util.Log
import com.example.spotify.data.local.*
import com.example.spotify.data.model.TopArtistsResponse
import com.example.spotify.data.network.SpotifyApiService
import com.example.spotify.data.repository.TopArtistsRepository

class GetTopArtistsUseCase(
    private val spotifyDAO: SpotifyDAO,
    private val apiService: SpotifyApiService,
    private val repository: TopArtistsRepository = TopArtistsRepository(apiService, spotifyDAO)
) {

    suspend fun getFromApi(
        accessToken: String,
        offset: Int = 0,
        timeRange: String = "medium_term"
    ): TopArtistsResponse {
        Log.d("GetTopArtistsUseCase", "Chamada API com: accessToken=Bearer $accessToken, offset=$offset, timeRange=$timeRange")
        val responseApi = repository.getTopArtistsApi(accessToken, offset, timeRange)
        mapToTopArtistsDB(responseApi, timeRange)
        return responseApi
    }

    private suspend fun mapToTopArtistsDB(response: TopArtistsResponse, timeRange: String) {
        val topArtistsDB = TopArtistsDB(
            total = response.total,
            limit = response.limit,
            offset = response.offset,
            href = response.href ?: "",
            next = response.next,
            previous = response.previous,
            timeRange = timeRange
        )
        val topArtistsId = repository.insertTopArtistsDB(topArtistsDB).toInt()

        // Inserindo artistas
        val artistsDB = response.items.mapNotNull { artist ->
            val existingArtistId = spotifyDAO.getArtistByApiId(artist.id)
            if (existingArtistId != null) {
                Log.d("MapToDB", "Artista já existe no banco: ${artist.name}, ID existente: $existingArtistId")
                null // Não insere novamente
            } else {
                Artist(
                    id = artist.id,
                    name = artist.name,
                    popularity = artist.popularity,
                    topArtistsId = topArtistsId
                )
            }
        }

        val artistsIds = repository.insertArtists(artistsDB)

        // Log para verificar artistas inseridos
        if (artistsIds.isNotEmpty()) {
            Log.d("MapToDB", "Artistas inseridos: ${artistsIds.size}")
        } else {
            Log.d("MapToDB", "Nenhum artista foi inserido no banco.")
        }

        // Inserindo imagens
        val imageArtistsDB = response.items.flatMapIndexed { index, artist ->
            artist.images.mapNotNull { image ->
                val artistId = artistsIds.getOrNull(index)?.toInt() ?: -1
                if (artistId == -1) {
                    Log.e("MapToDB", "Erro: Tentativa de vincular uma imagem a um artistId inválido para o artista ${artist.name}.")
                    null
                } else {
                    val imageExists = spotifyDAO.getImageCountForArtist(image.url, artistId) > 0
                    if (imageExists) {
                        Log.d("MapToDB", "Imagem já existe no banco para o artista $artistId: ${image.url}")
                        null
                    } else {
                        Log.d("MapToDB", "Atribuindo imagem '${image.url}' ao artistId $artistId")
                        ImageArtist(
                            url = image.url,
                            artistId = artistId
                        )
                    }
                }
            }
        }

        repository.insertImageArtists(imageArtistsDB)

        // Validação: Verificar imagens inseridas para o primeiro artista
        if (artistsIds.isNotEmpty()) {
            val firstArtistId = artistsIds.first().toInt()
            val testImages = spotifyDAO.getImagesForArtist(firstArtistId)
            Log.d("TestImages", "Imagens carregadas para o artista $firstArtistId: ${testImages.size}")
            testImages.forEach { image ->
                Log.d("TestImages", "URL da imagem: ${image.url}")
            }
        } else {
            Log.d("TestImages", "Nenhum artista foi inserido para teste de imagens.")
        }
    }

    suspend fun getFromDBWithOffsetAndLimit(
        limit: Int,
        offset: Int,
        timeRange: String = "medium_term"
    ): TopArtistsWithArtistsAndImages {
        Log.d("DebugUseCase", "Chamando getFromDBWithOffsetAndLimit() com limit=$limit, offset=$offset, timeRange=$timeRange")
        val dbResponse = repository.getTopArtistsDBWithOffsetAndLimit(limit, offset, timeRange)
        Log.d("DebugUseCase", "Artistas retornados do banco: ${dbResponse.artists.size}")

        // Logs detalhados para os artistas e imagens retornados
        dbResponse.artists.forEach { artistWithImages ->
            Log.d("DebugUseCase", "Artista: ${artistWithImages.artist.name} | ID: ${artistWithImages.artist.databaseId} | Imagens: ${artistWithImages.images.size}")
            artistWithImages.images.forEach { image ->
                Log.d("DebugUseCase", "URL da imagem: ${image.url}")
            }
        }
        return dbResponse
    }
}
