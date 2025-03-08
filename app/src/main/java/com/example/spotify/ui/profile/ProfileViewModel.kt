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

    private val _userProfile = MutableLiveData<UserProfile>()
    val userProfile: LiveData<UserProfile> get() = _userProfile

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    init {
        fetchUserProfile()
    }

    private fun fetchUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            val userProfile = getProfileUserUseCase.execute(accessToken)
            withContext(Dispatchers.Main) {
                if (userProfile != null) {
                    _userProfile.value = userProfile
                } else {
                    _error.value = "Error fetching user profile"
                }
            }
        }
    }
}
