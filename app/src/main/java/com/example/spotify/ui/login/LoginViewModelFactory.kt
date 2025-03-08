package com.example.spotify.ui

import LoginViewModel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                context,
                getAccessTokenUseCase = TODO()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
