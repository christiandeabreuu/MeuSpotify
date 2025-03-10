package com.example.spotify.domain.usecase

import CreatePlaylistRepository

class CreatePlaylistUseCase(private val createPlaylistRepository: CreatePlaylistRepository) {
    suspend fun execute(accessToken: String, playlistName: String): Result<String> {
        return try {
            val response = createPlaylistRepository.createPlaylist(accessToken, playlistName)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
