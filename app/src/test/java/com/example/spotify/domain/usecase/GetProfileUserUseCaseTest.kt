package com.example.spotify.domain.usecase

import com.example.spotify.data.local.SpotifyDAO
import com.example.spotify.data.model.Image
import com.example.spotify.data.model.UserProfile
import com.example.spotify.data.network.SpotifyApiService
import com.example.spotify.data.repository.UserProfileRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetProfileUserUseCaseTest {

    private val spotifyDAO = mockk<SpotifyDAO>()
    private val apiService = mockk<SpotifyApiService>()
    private val repository = mockk<UserProfileRepository>()
    private val useCase = GetProfileUserUseCase(spotifyDAO, apiService, repository)


    @Test
    fun `getUserProfileFromApi should return null when both API and DB fail`() = runBlocking {
        // Arrange
        val accessToken = "valid_token"

        // Mockando o comportamento do repositório
        coEvery { repository.getUserProfileFromApi(accessToken) } throws Exception("API Error")
        coEvery { repository.getUserProfileFromDB() } returns null

        // Act
        val result = useCase.getUserProfileFromApi(accessToken)

        // Assert
        assertEquals(null, result)
        coVerify { repository.getUserProfileFromApi(accessToken) }
        coVerify { repository.getUserProfileFromDB() }
    }
}
