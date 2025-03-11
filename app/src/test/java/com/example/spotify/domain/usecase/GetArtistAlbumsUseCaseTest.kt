package com.example.spotify.domain.usecase

import com.example.spotify.data.model.ImageArtist
import com.example.spotify.data.model.Album
import com.example.spotify.data.model.AlbumsResponse
import com.example.spotify.data.network.SpotifyApiService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response

class GetArtistAlbumsUseCaseTest {

    private val spotifyApiService = mockk<SpotifyApiService>()
    private val useCase = GetArtistAlbumsUseCase(spotifyApiService)


    @Test
    fun `execute should return null when exception is thrown`() = runBlocking {
        // Arrange
        val accessToken = "valid_token"
        val artistId = "artist_123"

        coEvery {
            spotifyApiService.getArtistAlbums("Bearer $accessToken", artistId)
        } throws Exception("Network error")

        // Act
        val result = useCase.execute(accessToken, artistId)

        // Assert
        assertEquals(null, result)
    }
}
