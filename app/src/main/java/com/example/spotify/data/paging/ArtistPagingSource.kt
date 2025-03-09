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
    private val accessToken: String // Receba o token aqui
) : PagingSource<Int, Artist>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Artist> {
        try {
            val nextPageNumber = params.key ?: 1
            Log.d("ArtistPagingSource", "Carregando artistas com token: Bearer $accessToken")
            val response = useCaseTopArtists.execute("Bearer $accessToken", nextPageNumber) // Use o token correto aqui
            return LoadResult.Page(
                data = response.firstOrNull()?.items ?: emptyList(),
                prevKey = null,
                nextKey = response.firstOrNull()?.offset
            )
        } catch (e: Exception) {
            Log.e("ArtistPagingSource", "Erro: ${e.message}")
            return LoadResult.Error(e)
        }

    }


    override fun getRefreshKey(state: PagingState<Int, Artist>): Int? {
        // Try to find the page key of the closest page to anchorPosition from
        // either the prevKey or the nextKey; you need to handle nullability
        // here.
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey are null -> anchorPage is the
        //    initial page, so return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(20) ?: anchorPage?.nextKey?.minus(20)
        }
    }
}