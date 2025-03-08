package com.example.spotify.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.data.model.UserProfile
import com.example.spotify.domain.usecase.GetProfileUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(
    private val getProfileUserUseCase: GetProfileUserUseCase,
    private val accessToken: String
) : ViewModel() {

    private val _userProfile = MutableLiveData<Result<UserProfile>>()
    val userProfile: LiveData<Result<UserProfile>> get() = _userProfile

    init {
        fetchUserProfile()
    }

    private fun fetchUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userProfile = getProfileUserUseCase.execute(accessToken)
                _userProfile.postValue(Result.success(userProfile) as Result<UserProfile>?)
            } catch (e: Exception) {
                _userProfile.postValue(Result.failure(e))
            }
        }
    }
}

