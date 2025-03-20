package com.example.spotify.data.paging

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.spotify.data.model.Artist
import com.example.spotify.data.model.ImageArtist
import com.example.spotify.domain.usecase.GetTopArtistsUseCase

class ArtistPagingSource(
    private val useCaseTopArtists: GetTopArtistsUseCase,
    private val accessToken: String,
    private val context: Context
) : PagingSource<Int, Artist>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Artist> {
        return try {
            val nextPageNumber = params.key ?: 0
            Log.d("PagingSource", "Carregando artistas com offset: $nextPageNumber")

            // Verificar o modo de operação (online/offline)
            val response: List<Artist> = if (checkInternetConnection(context)) {
                // Dados da API
                useCaseTopArtists.getFromApi(accessToken, nextPageNumber).items ?: emptyList()
            } else {
                // Dados do banco de dados
                useCaseTopArtists.getFromDBWithOffsetAndLimit(params.loadSize, nextPageNumber).artists.map { artistWithImages ->
                    Log.d("PagingSource", "Artista offline: ${artistWithImages.artist.name} | Imagens: ${artistWithImages.images.size}")
                    Artist(
                        id = artistWithImages.artist.id,
                        name = artistWithImages.artist.name,
                        popularity = artistWithImages.artist.popularity,
                        images = artistWithImages.images.map { image ->
                            ImageArtist(url = image.url)
                        }
                    )
                }
            }

            Log.d("PagingSource", "Artistas carregados: ${response.size}")

            // Retornar a página de dados
            return LoadResult.Page(
                data = response,
                prevKey = if (nextPageNumber == 0) null else nextPageNumber - params.loadSize,
                nextKey = if (response.isEmpty()) null else nextPageNumber + params.loadSize
            )
        } catch (e: Exception) {
            Log.e("PagingSource", "Erro ao carregar artistas: ${e.message}")
            LoadResult.Error(e)
        }
    }



    override fun getRefreshKey(state: PagingState<Int, Artist>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(20) ?: anchorPage?.nextKey?.minus(20)
        }
    }

    // Função utilitária para verificar conexão com a internet
    private fun checkInternetConnection(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

}

