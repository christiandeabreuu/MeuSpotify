package com.example.spotify.domain.usecase

import com.example.spotify.data.model.Image
import com.example.spotify.data.model.UserProfile
import com.example.spotify.data.network.SpotifyApiService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetUserProfilePlaylistUseCaseTest {

    private val apiService = mockk<SpotifyApiService>()
    private val useCase = GetUserProfilePlaylistUseCase(apiService)

    @Test
    fun `getUserProfile should return user profile when API call is successful`() = runBlocking {
        // Arrange
        val accessToken = "valid_token"
        val listImageMockk = listOf(Image("url"))
        val expectedUserProfile = UserProfile("123", "John Doe", listImageMockk)

        // Mockando o comportamento da API
        coEvery { apiService.getUserProfile("Bearer $accessToken") } returns expectedUserProfile

        // Act
        val result = useCase.getUserProfile(accessToken)

        // Assert
        assertEquals(expectedUserProfile, result)
        coVerify { apiService.getUserProfile("Bearer $accessToken") }
    }

    @Test
    fun `getUserProfile should return null when API call throws exception`() = runBlocking {
        // Arrange
        val accessToken = "invalid_token"

        // Mockando uma exceção da API
        coEvery { apiService.getUserProfile("Bearer $accessToken") } throws Exception("API Error")

        // Act
        val result = useCase.getUserProfile(accessToken)

        // Assert
        assertEquals(null, result)
        coVerify { apiService.getUserProfile("Bearer $accessToken") }
    }
}
