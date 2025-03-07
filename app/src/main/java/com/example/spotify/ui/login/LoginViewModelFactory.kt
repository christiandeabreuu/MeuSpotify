import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spotify.auth.SpotifyAuthHelper
import com.example.spotify.data.repository.AuthRepositoryImpl
import com.example.spotify.domain.usecase.GetAccessTokenUseCase

class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            val authHelper = SpotifyAuthHelper(context)
            val authRepository = AuthRepositoryImpl(context, authHelper) // Certifique-se de passar ambos os parâmetros
            val getAccessTokenUseCase = GetAccessTokenUseCase(authRepository)
            return LoginViewModel(context, getAccessTokenUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
