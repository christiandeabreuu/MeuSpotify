    package com.example.spotify.data.model

    import android.content.Context


    class SpotifyTokens(private val context: Context) {

        // Classe de dados para armazenar os tokens
        data class Tokens(val accessToken: String, val refreshToken: String)
    }
