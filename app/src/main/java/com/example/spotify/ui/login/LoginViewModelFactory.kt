package com.example.spotify.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spotify.data.repository.AuthRepositoryImpl
import com.example.spotify.domain.usecase.GetAccessTokenUseCase
import com.example.spotify.ui.login.LoginViewModel

class LoginViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            val getAccessTokenUseCase = GetAccessTokenUseCase(
                repository = AuthRepositoryImpl(context)
            )
            return LoginViewModel(
                context = context,
                getAccessTokenUseCase = getAccessTokenUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
