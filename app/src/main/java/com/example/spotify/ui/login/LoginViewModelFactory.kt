import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spotify.data.repository.AuthRepositoryImpl
import com.example.spotify.domain.usecase.GetAccessTokenUseCase
import com.example.spotify.auth.SpotifyAuthHelper

class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            val authHelper = SpotifyAuthHelper(context)
            val authRepository = AuthRepositoryImpl(authHelper)
            val getAccessTokenUseCase = GetAccessTokenUseCase(authRepository)
            return LoginViewModel(context, getAccessTokenUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
