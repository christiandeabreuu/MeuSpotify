package com.example.spotify.domain.usecase

import com.example.spotify.data.model.Tokens
import com.example.spotify.data.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class RefreshAccessTokenUseCaseTest {

    private val repository = mockk<AuthRepository>()
    private val useCase = RefreshAccessTokenUseCase(repository)

    @Test
    fun `execute should return tokens when repository call is successful`() = runBlocking {
        // Arrange
        val refreshToken = "valid_refresh_token"
        val expectedTokens = Tokens("new_access_token", "new_refresh_token")

        // Mockando o comportamento do repositório
        coEvery { repository.refreshAccessToken(refreshToken) } returns expectedTokens

        // Act
        val result = useCase.execute(refreshToken)

        // Assert
        assertEquals(expectedTokens, result)
        coVerify { repository.refreshAccessToken(refreshToken) } // Verifica se o método foi chamado
    }

    @Test
    fun `execute should return null when repository call throws exception`() = runBlocking {
        // Arrange
        val refreshToken = "invalid_refresh_token"

        // Mockando o comportamento do repositório para lançar uma exceção
        coEvery { repository.refreshAccessToken(refreshToken) } throws Exception("Invalid token")

        // Act
        val result = useCase.execute(refreshToken)

        // Assert
        assertEquals(null, result)
        coVerify { repository.refreshAccessToken(refreshToken) } // Verifica se o método foi chamado
    }
}
