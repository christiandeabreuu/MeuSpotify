package com.example.spotify.data.paging

import androidx.paging.PagingState
import com.example.spotify.data.local.Artist
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class PagingSourceTest {

    @Test
    fun `getRefreshKey retorna null quando anchorPosition e pages são nulos`() {
        val state: PagingState<Int, Artist> = mockk()

        every { state.anchorPosition } returns null
        every { state.closestPageToPosition(any()) } returns null

        val result = object {
            fun getRefreshKey(state: PagingState<Int, Artist>): Int? {
                return state.anchorPosition?.let { anchorPosition ->
                    val anchorPage = state.closestPageToPosition(anchorPosition)
                    anchorPage?.prevKey?.plus(20) ?: anchorPage?.nextKey?.minus(20)
                }
            }
        }.getRefreshKey(state)

        assertEquals(null, result)
    }
}
