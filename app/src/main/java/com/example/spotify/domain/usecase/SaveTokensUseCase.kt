package com.example.spotify.domain.usecase


import com.example.spotify.auth.AuthRepository

class SaveTokensUseCase(private val repository: AuthRepository) {
    fun execute(accessToken: String, refreshToken: String) {
        repository.saveTokens(accessToken, refreshToken)
    }
}

