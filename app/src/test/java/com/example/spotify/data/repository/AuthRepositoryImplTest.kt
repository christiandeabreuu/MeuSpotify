package com.example.spotify.data.repository

import android.content.SharedPreferences
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class TokenLoaderTest {

    private val sharedPreferences: SharedPreferences = mockk() // Mock do SharedPreferences
    private val sharedPreferencesEditor: SharedPreferences.Editor = mockk()

    @Test
    fun `loadTokens deve retornar accessToken e refreshToken corretos`() {
        every { sharedPreferences.getString("ACCESS_TOKEN", "") } returns "meuAccessToken"
        every { sharedPreferences.getString("REFRESH_TOKEN", "") } returns "meuRefreshToken"

        val result = object {
            fun loadTokens(): Pair<String, String> {
                val accessToken = sharedPreferences.getString("ACCESS_TOKEN", "") ?: ""
                val refreshToken = sharedPreferences.getString("REFRESH_TOKEN", "") ?: ""
                return Pair(accessToken, refreshToken)
            }
        }.loadTokens()

        assertEquals(Pair("meuAccessToken", "meuRefreshToken"), result)
    }

    @Test
    fun `loadTokens deve retornar pares vazios se SharedPreferences estiver vazio`() {
        every { sharedPreferences.getString("ACCESS_TOKEN", "") } returns ""
        every { sharedPreferences.getString("REFRESH_TOKEN", "") } returns ""

        val result = object {
            fun loadTokens(): Pair<String, String> {
                val accessToken = sharedPreferences.getString("ACCESS_TOKEN", "") ?: ""
                val refreshToken = sharedPreferences.getString("REFRESH_TOKEN", "") ?: ""
                return Pair(accessToken, refreshToken)
            }
        }.loadTokens()
        assertEquals(Pair("", ""), result)
    }
}
