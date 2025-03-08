package com.example.spotify.domain.usecase


import com.example.spotify.data.repository.AuthRepository

class SaveTokensUseCase(private val repository: AuthRepository) {
    fun execute(accessToken: String, refreshToken: String) {
        repository.saveTokens(accessToken, refreshToken)
    }
}
