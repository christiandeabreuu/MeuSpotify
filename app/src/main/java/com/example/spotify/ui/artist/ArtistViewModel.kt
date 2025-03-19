package com.example.spotify.ui.artist

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.spotify.data.local.ArtistWithImages
import com.example.spotify.data.model.Artist
import com.example.spotify.data.model.Tokens
import com.example.spotify.data.model.TopArtistsResponse
import com.example.spotify.data.paging.ArtistPagingSource
import com.example.spotify.domain.usecase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class ArtistViewModel(
    private val loadTokensUseCase: LoadTokensUseCase,
    private val saveTokensUseCase: SaveTokensUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val refreshAccessTokenUseCase: RefreshAccessTokenUseCase,
    private val getTopArtistsUseCase: GetTopArtistsUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val context: Context
) : ViewModel() {

    fun loadTokens() = liveData(Dispatchers.IO) {
        try {
            val tokens = loadTokensUseCase.execute()
            emit(Result.success(tokens))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun exchangeCodeForTokens(authorizationCode: String, redirectUri: String) = liveData(Dispatchers.IO) {
        try {

            val tokens: Tokens = getAccessTokenUseCase.execute(authorizationCode, redirectUri)
            emit(Result.success(tokens))
        } catch (e: Exception) {
            emit(Result.failure<Tokens>(e))
        }
    }

    fun saveAccessToken(accessToken: String, refreshToken: String) {
        saveTokensUseCase.execute(accessToken, refreshToken)
    }

    fun getUserProfile(accessToken: String) = liveData(Dispatchers.IO) {
        try {
            val userProfile = getUserProfileUseCase.execute(accessToken)
            emit(Result.success(userProfile))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun refreshToken(refreshToken: String) = liveData(Dispatchers.IO) {
        try {
            val tokens = refreshAccessTokenUseCase.execute(refreshToken)
            emit(Result.success(tokens))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun getTopArtist(accessToken: String) = liveData(Dispatchers.IO) {
        try {
            val topArtists = getTopArtistsUseCase.getFromApi(accessToken)
            emit(Result.success(topArtists))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun getArtistsPagingData(accessToken: String): Flow<PagingData<Artist>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ArtistPagingSource(getTopArtistsUseCase, accessToken, context) } // O token correto deve vir aqui
        ).flow.cachedIn(viewModelScope)
    }
}
