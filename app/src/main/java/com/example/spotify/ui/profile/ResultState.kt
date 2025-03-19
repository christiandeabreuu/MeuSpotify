package com.example.spotify.ui.profile

class ResultState {
    sealed class ResultState<out T> {
        data class Success<out T>(val data: T) : ResultState<T>()
        data class Error(val error: Throwable) : ResultState<Nothing>()
        object Empty : ResultState<Nothing>()
    }

}