package com.example.spotify.ui.login

import com.example.spotify.data.model.Tokens

data class TokenState(val token: Tokens? = null, val event: TokenStateEvent = TokenStateEvent.Loading)

sealed interface TokenStateEvent {
    data object GetToken : TokenStateEvent
    data object Loading : TokenStateEvent
}