package com.example.spotify.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.spotify.data.model.Artist
import com.example.spotify.data.model.TopArtistsResponse
import com.example.spotify.data.network.SpotifyApiService
import com.example.spotify.domain.usecase.GetTopArtistsUseCase
import kotlinx.coroutines.flow.firstOrNull


class ArtistPagingSource(
    private val useCaseTopArtists: GetTopArtistsUseCase,
    private val accessToken: String
) : PagingSource<Int, Artist>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Artist> {
        return try {
            val nextPageNumber = params.key ?: 0 // Inicia na página 0
            Log.d("ArtistPagingSource", "Carregando artistas com token: $accessToken, offset: $nextPageNumber")

            // Chama o use case para obter os artistas
            val response = useCaseTopArtists.execute(accessToken, nextPageNumber)

            LoadResult.Page(
                data = response.items ?: emptyList(), // Lista de artistas
                prevKey = if (nextPageNumber == 0) null else nextPageNumber - response.limit, // Previous page
                nextKey = if (response.next != null) nextPageNumber + response.limit else null // Próxima página
            )
        } catch (e: Exception) {
            Log.e("ArtistPagingSource", "Erro ao carregar artistas: ${e.message}")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Artist>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(20) ?: anchorPage?.nextKey?.minus(20)
        }
    }
}
