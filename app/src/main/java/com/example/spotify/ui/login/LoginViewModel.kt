import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.spotify.auth.SpotifyAuthHelper.Tokens
import com.example.spotify.domain.usecase.GetAccessTokenUseCase
import kotlinx.coroutines.Dispatchers

class LoginViewModel(
    private val context: Context,
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) : ViewModel() {

    fun handleRedirect(uri: Uri, redirectUri: String) = liveData(Dispatchers.IO) {
        val authorizationCode = uri.getQueryParameter("code")
        if (authorizationCode != null) {
            try {
                val tokens: Tokens = getAccessTokenUseCase.execute(authorizationCode)
                emit(Result.success(tokens))
            } catch (e: Exception) {
                emit(Result.failure<Tokens>(e))
            }
        } else {
            emit(Result.failure<Tokens>(Exception("Código de autorização não encontrado")))
        }
    }

    fun saveTokens(accessToken: String, refreshToken: String) {
        val sharedPreferences = context.getSharedPreferences("SpotifyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("ACCESS_TOKEN", accessToken)
        editor.putString("REFRESH_TOKEN", refreshToken)
        editor.apply()
    }
}
