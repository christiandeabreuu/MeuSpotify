package com.example.spotify.domain.usecase

import android.util.Log
import com.example.spotify.data.repository.AuthRepository


class SaveTokensUseCase(private val repository: AuthRepository) {
    fun execute(accessToken: String, refreshToken: String) {
        Log.d("SaveTokensUseCase", "Salvando tokens: accessToken=$accessToken, refreshToken=$refreshToken")
        repository.saveTokens(accessToken, refreshToken)
        Log.d("SaveTokensUseCase", "Tokens salvos com sucesso")
    }
}


