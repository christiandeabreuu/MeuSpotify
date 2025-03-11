package com.example.spotify.domain.usecase

import CreatePlaylistRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class CreatePlaylistUseCaseTest {

    private val createPlaylistRepository = mockk<CreatePlaylistRepository>()
    private val createPlaylistUseCase = CreatePlaylistUseCase(createPlaylistRepository)

    @Test
    fun `execute should return success when repository creates playlist successfully`() = runBlocking {
        // Arrange
        val accessToken = "valid_token"
        val playlistName = "My Playlist"
        val expectedResponse = "Playlist Created Successfully"

        coEvery { createPlaylistRepository.createPlaylist(accessToken, playlistName) } returns expectedResponse

        // Act
        val result = createPlaylistUseCase.execute(accessToken, playlistName)

        // Assert
        assertEquals(Result.success(expectedResponse), result)
    }

    @Test
    fun `execute should return failure when repository throws exception`() = runBlocking {
        // Arrange
        val accessToken = "valid_token"
        val playlistName = "My Playlist"
        val expectedException = Exception("Network Error")

        coEvery { createPlaylistRepository.createPlaylist(accessToken, playlistName) } throws expectedException

        // Act
        val result = createPlaylistUseCase.execute(accessToken, playlistName)

        // Assert
        assertEquals(Result.failure<String>(expectedException).exceptionOrNull(), result.exceptionOrNull())
    }
}
