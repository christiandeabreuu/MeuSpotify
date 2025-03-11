package com.example.spotify.ui.profile

import android.util.Log
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
                Log.d("ProfileViewModel", "Iniciando busca do perfil do usuário")
                val userProfile = getProfileUserUseCase.getUserProfileFromApi(accessToken) // Use Case decide API ou Banco
                if (userProfile != null) {
                    Log.d("ProfileViewModel", "Perfil do usuário recebido: $userProfile")
                    _userProfile.postValue(Result.success(userProfile))
                } else {
                    Log.e("ProfileViewModel", "Perfil retornado como nulo")
                    _userProfile.postValue(Result.failure(Exception("Nenhum dado encontrado")))
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Erro ao buscar perfil: ${e.message}")
                _userProfile.postValue(Result.failure(e))
            }
        }
    }
}

