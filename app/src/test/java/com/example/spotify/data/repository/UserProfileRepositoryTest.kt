package com.example.spotify.data.repository

import com.example.spotify.data.local.SpotifyDAO
import com.example.spotify.data.model.Image
import com.example.spotify.data.model.UserProfile
import com.example.spotify.data.network.SpotifyApiService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class UserProfileRepositoryTest {
    private val dao = mockk<SpotifyDAO>() //
    private val apiService: SpotifyApiService = mockk() // Mockando o ApiService
    private val repository = UserProfileRepository(apiService, dao) // Sua classe que contém a função

    @Test
    fun `getUserProfileFromApi deve retornar UserProfile com sucesso`() = runBlocking {
        // Mock da resposta da API
        val listImage = listOf(Image("url1"))
        val mockUserProfile = UserProfile("id123", "Nome Teste", listImage)
        coEvery { apiService.getUserProfile("Bearer tokenValido") } returns mockUserProfile

        // Chamando a função
        val result = repository.getUserProfileFromApi("tokenValido")

        // Validando o resultado
        assertEquals(mockUserProfile, result)
    }

    @Test
    fun `getUserProfileFromApi deve retornar null em caso de erro`() = runBlocking {
        // Simulando uma exceção ao chamar a API
        coEvery { apiService.getUserProfile("Bearer tokenInvalido") } throws Exception("Erro na API")

        // Chamando a função
        val result = repository.getUserProfileFromApi("tokenInvalido")

        // Validando o resultado
        assertNull(result)
    }
}
