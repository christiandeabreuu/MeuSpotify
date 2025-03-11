package com.example.spotify.domain.usecase

import com.example.spotify.data.repository.AuthRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class SaveTokensUseCaseTest {

    private val repository = mockk<AuthRepository>(relaxed = true) // `relaxed` permite ignorar a implementação detalhada
    private val useCase = SaveTokensUseCase(repository)

    @Test
    fun `execute should call repository to save tokens`() {
        // Arrange
        val accessToken = "test_access_token"
        val refreshToken = "test_refresh_token"

        // Act
        useCase.execute(accessToken, refreshToken)

        // Assert
        verify { repository.saveTokens(accessToken, refreshToken) } // Verifica se o método foi chamado com os parâmetros corretos
    }
}
