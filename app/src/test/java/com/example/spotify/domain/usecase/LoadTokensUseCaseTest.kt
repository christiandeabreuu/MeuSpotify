package com.example.spotify.domain.usecase

import com.example.spotify.data.repository.AuthRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class LoadTokensUseCaseTest {

    private val repository = mockk<AuthRepository>()
    private val useCase = LoadTokensUseCase(repository)

    @Test
    fun `execute should return tokens from repository`() {
        // Arrange
        val expectedTokens = Pair("access_token", "refresh_token")

        every { repository.loadTokens() } returns expectedTokens

        // Act
        val result = useCase.execute()

        // Assert
        assertEquals(expectedTokens, result)
        verify { repository.loadTokens() } // Verifica se o método foi chamado
    }
}
