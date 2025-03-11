package com.example.spotify.domain.usecase

import com.example.spotify.data.model.Tokens
import com.example.spotify.data.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetAccessTokenUseCaseTest {

    private val repository = mockk<AuthRepository>()
    private val useCase = GetAccessTokenUseCase(repository)

    @Test
    fun `execute should call repository and return tokens`() = runBlocking {
        // Arrange
        val authorizationCode = "authCode123"
        val redirectUri = "https://redirect.uri"
        val expectedTokens = Tokens(accessToken = "accessToken123", refreshToken = "refreshToken123")

        coEvery { repository.getAccessToken(authorizationCode, redirectUri) } returns expectedTokens

        // Act
        val result = useCase.execute(authorizationCode, redirectUri)

        // Assert
        assertEquals(expectedTokens, result)

        // Verify
        coVerify { repository.getAccessToken(authorizationCode, redirectUri) }
    }
}
