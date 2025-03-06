package com.example.spotify.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spotify.ui.albuns.AlbumsViewModel

class AlbumsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlbumsViewModel::class.java)) {
            return AlbumsViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
