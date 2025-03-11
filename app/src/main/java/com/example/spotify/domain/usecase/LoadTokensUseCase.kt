package com.example.spotify.domain.usecase

import android.util.Log
import com.example.spotify.data.repository.AuthRepository

class LoadTokensUseCase(private val repository: AuthRepository) {
    fun execute(): Pair<String, String> {
        val tokens = repository.loadTokens()
        Log.d(
            "LoadTokensUseCase",
            "Tokens carregados: accessToken=${tokens.first}, refreshToken=${tokens.second}"
        )
        return tokens
    }
}
