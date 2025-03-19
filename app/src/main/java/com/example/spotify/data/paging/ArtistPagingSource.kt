package com.example.spotify.data.paging

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.spotify.data.model.Artist
import com.example.spotify.domain.usecase.GetTopArtistsUseCase

class ArtistPagingSource(
    private val useCaseTopArtists: GetTopArtistsUseCase,
    private val accessToken: String,
    private val context: Context
) : PagingSource<Int, Artist>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Artist> {
        return try {
            val nextPageNumber = params.key ?: 0 // Página inicial é 0
            Log.d("ArtistPagingSource", "Carregando artistas com offset: $nextPageNumber")

            val isOnline = checkInternetConnection(context) // Verifica conectividade
            val response: List<Artist> = if (isOnline) {
                // Busca da API e salva no banco
                val apiResponse = useCaseTopArtists.getFromApi(accessToken, nextPageNumber)
                apiResponse.items ?: emptyList()
            } else {
                // Busca do banco (offline)
                val dbResponse = useCaseTopArtists.getFromDBWithOffsetAndLimit(20, nextPageNumber)
                val response = dbResponse.artists.map { artistWithImages ->
                    Artist(
                        id = artistWithImages.artist.id,
                        name = artistWithImages.artist.name,
                        popularity = artistWithImages.artist.popularity,
                        images = artistWithImages.images.map { image ->
                            com.example.spotify.data.model.ImageArtist(
                                url = image.url
                            )
                        }
                    )
                }

                response
            }


            LoadResult.Page(
                data = response,
                prevKey = if (nextPageNumber == 0) null else nextPageNumber - 20,
                nextKey = if (response.isEmpty()) null else nextPageNumber + 20
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

    // Função utilitária para verificar conexão com a internet
    private fun checkInternetConnection(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

}

