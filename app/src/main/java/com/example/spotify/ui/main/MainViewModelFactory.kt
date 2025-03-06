package com.example.spotify.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spotify.auth.SpotifyAuthHelper

class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(SpotifyAuthHelper(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
