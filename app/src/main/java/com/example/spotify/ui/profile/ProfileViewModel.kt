package com.example.spotify.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.data.model.UserProfile
import com.example.spotify.domain.usecase.GetProfileUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(
    private val getProfileUserUseCase: GetProfileUserUseCase
) : ViewModel() {

    private val _userProfile = MutableStateFlow<Result<UserProfile>?>(null)
    val userProfile: StateFlow<Result<UserProfile>?> get() = _userProfile

    fun loadUserProfile(accessToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val profile = getProfileUserUseCase.getUserProfileFromApi(accessToken)
                if (profile != null) {
                    _userProfile.emit(Result.success(profile))
                } else {
                    _userProfile.emit(Result.failure(Exception("Perfil do usuário está nulo")))
                }
            } catch (e: Exception) {
                _userProfile.emit(Result.failure(e))
            }
        }
    }


}



