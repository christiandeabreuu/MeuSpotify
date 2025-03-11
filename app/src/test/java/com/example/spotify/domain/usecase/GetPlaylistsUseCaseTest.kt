package com.example.spotify.domain.usecase

import GetPlaylistsUseCase
import PlaylistRepository
import com.example.spotify.data.local.PlaylistDB
import com.example.spotify.data.model.Playlist
import com.example.spotify.data.network.SpotifyApiService
import com.example.spotify.data.local.SpotifyDAO
import com.example.spotify.data.model.Owner
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetPlaylistsUseCaseTest {

    private val spotifyDAO = mockk<SpotifyDAO>()
    private val apiService = mockk<SpotifyApiService>()
    private val repository = mockk<PlaylistRepository>()
    private lateinit var useCase: GetPlaylistsUseCase

    @Before
    fun setup() {
        useCase = GetPlaylistsUseCase(spotifyDAO, apiService, repository)
    }



    @Test
    fun `getFromApi should return empty list when repository returns null`() = runBlocking {
        // Arrange
        val accessToken = "valid_token"

        coEvery { repository.getPlaylistsFromApi(accessToken) } returns null

        // Act
        val result = useCase.getFromApi(accessToken)

        // Assert
        assertEquals(emptyList<Playlist>(), result)
        coVerify { repository.getPlaylistsFromApi(accessToken) }
    }

}
