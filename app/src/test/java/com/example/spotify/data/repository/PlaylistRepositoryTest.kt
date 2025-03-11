package com.example.spotify.data.repository

import PlaylistRepository
import com.example.spotify.data.local.SpotifyDAO
import com.example.spotify.data.network.SpotifyApiService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNull
import org.junit.Test

class PlaylistRepositoryTest {

    private val spotifyDAO: SpotifyDAO = mockk()
    private val apiService: SpotifyApiService = mockk()
    private val repository = PlaylistRepository(spotifyDAO, apiService)

    @Test
    fun `getPlaylistsFromApi deve retornar null em caso de erro`() = runBlocking {
        coEvery {
            apiService.getPlaylists("Bearer tokenInvalido")
        } throws Exception("Erro ao buscar playlists")

        val result = repository.getPlaylistsFromApi("tokenInvalido")

        assertNull(result)
    }
}
