package com.example.spotify.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.spotify.data.local.ArtistWithImages
import com.example.spotify.domain.usecase.GetTopArtistsUseCase


class ArtistPagingSource(
    private val useCaseTopArtists: GetTopArtistsUseCase,
    private val accessToken: String
) : PagingSource<Int, ArtistWithImages>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArtistWithImages> {
        return try {
            val nextPageNumber = params.key ?: 0
            val offset = nextPageNumber * 20
            val limit = params.loadSize
            Log.d("ArtistPagingSource", "load() chamado com limit: $limit, offset: $offset, timeRange: medium_term")
            val response = useCaseTopArtists.getFromDBWithOffsetAndLimit(limit, offset)
            Log.d("ArtistPagingSource", "load() response.artists.size: ${response.artists.size}")
            val artists = response.artists
            LoadResult.Page(
                data = artists,
                prevKey = if (nextPageNumber == 0) null else nextPageNumber - 1,
                nextKey = if (artists.isEmpty()) null else nextPageNumber + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ArtistWithImages>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
