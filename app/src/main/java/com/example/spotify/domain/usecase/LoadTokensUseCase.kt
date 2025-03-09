package com.example.spotify.domain.usecase

import com.example.spotify.data.repository.AuthRepository
import android.util.Log


class LoadTokensUseCase(private val repository: AuthRepository) {
    fun execute(): Pair<String, String> {
        val tokens = repository.loadTokens()
        Log.d("LoadTokensUseCase", "Tokens carregados: accessToken=${tokens.first}, refreshToken=${tokens.second}")
        return tokens
    }
}
