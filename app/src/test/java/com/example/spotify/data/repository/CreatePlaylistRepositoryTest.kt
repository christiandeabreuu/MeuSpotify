package com.example.spotify.data.repository

import CreatePlaylistRepository
import com.example.spotify.data.model.CreatePlaylistRequest
import com.example.spotify.data.network.SpotifyApiService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class CreatePlaylistRepositoryTest {

    private val apiService: SpotifyApiService = mockk() // Mock do SpotifyApiService
    private val repository =
        CreatePlaylistRepository(apiService) // Repositório com o mock do serviço


    @Test
    fun `createPlaylist deve lançar exceção em caso de erro`() = runBlocking {
        // Mockando o request e erro no response
        val playlistName = "Minha Playlist"
        val accessToken = "tokenInvalido"
        val requestBody = CreatePlaylistRequest(name = playlistName, public = true)

        coEvery {
            apiService.createPlaylist("Bearer $accessToken", requestBody)
        } throws Exception("Erro ao criar playlist: Token inválido")

        // Validando a exceção
        try {
            repository.createPlaylist(accessToken, playlistName)
            fail("Esperava uma exceção, mas não foi lançada")
        } catch (e: Exception) {
            assertEquals("Erro ao criar playlist: Token inválido", e.message)
        }
    }
}
