package com.example.spotify.domain.usecase

import PlaylistRepository

class CreatePlaylistUseCase(private val playlistRepository: PlaylistRepository) {
    suspend fun execute(accessToken: String, playlistName: String): Result<String> {
        return try {
            val response = playlistRepository.createPlaylist(accessToken, playlistName)
            Result.success(response) // Retorna uma mensagem de sucesso
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
