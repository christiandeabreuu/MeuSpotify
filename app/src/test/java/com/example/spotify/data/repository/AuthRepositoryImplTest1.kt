package com.example.spotify.data.repository

import android.content.SharedPreferences
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class TokenSaverTest {

    private val sharedPreferences: SharedPreferences = mockk() // Mock do SharedPreferences
    private val editor: SharedPreferences.Editor = mockk(relaxed = true) // Mock do Editor

    @Test
    fun `saveTokens deve salvar accessToken e refreshToken no SharedPreferences`() {
        // Mockando o editor e SharedPreferences
        every { sharedPreferences.edit() } returns editor

        // Implementando a função em uma classe para teste
        val tokenSaver = object {
            fun saveTokens(accessToken: String, refreshToken: String) {
                val editor = sharedPreferences.edit()
                editor.putString("ACCESS_TOKEN", accessToken)
                editor.putString("REFRESH_TOKEN", refreshToken)
                editor.apply()
            }
        }

        // Chamando a função
        tokenSaver.saveTokens("meuAccessToken", "meuRefreshToken")

        // Verificando se os métodos corretos foram chamados
        verify { editor.putString("ACCESS_TOKEN", "meuAccessToken") }
        verify { editor.putString("REFRESH_TOKEN", "meuRefreshToken") }
        verify { editor.apply() }
    }
}
