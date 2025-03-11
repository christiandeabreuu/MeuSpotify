package com.example.spotify.data.repository

import com.example.spotify.data.local.SpotifyDAO
import com.example.spotify.data.network.SpotifyApiService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class TopArtistsApiTest {
    private val dao = mockk<SpotifyDAO>() // Mock do SpotifyDAO
    private val apiService: SpotifyApiService = mockk() // Mock do ApiService
    private val repository = TopArtistsRepository(
        apiService,
        dao
    ) // Substitua pelo nome real da classe que contém sua função


    @Test(expected = Exception::class)
    fun `getTopArtistsApi deve lançar exceção em caso de erro da API`(): Unit = runBlocking {
        // Simulando um erro na chamada da API
        coEvery {
            apiService.getTopArtists(
                accessToken = "Bearer tokenInvalido",
                limit = 20,
                timeRange = "medium_term",
                offset = 0
            )
        } throws Exception("Erro ao buscar artistas")

        // Chamando a função (espera-se que lance uma exceção)
        repository.getTopArtistsApi("tokenInvalido")
    }
}
