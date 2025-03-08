package com.example.spotify.data.repository


import android.content.Context
import com.example.spotify.data.model.SpotifyTokens.Tokens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class AuthRepositoryImpl(private val context: Context) : AuthRepository {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val sharedPreferences = context.getSharedPreferences("SpotifyPrefs", Context.MODE_PRIVATE)

    override suspend fun getAccessToken(authorizationCode: String): Tokens {
        val requestBody = FormBody.Builder()
            .add("grant_type", "authorization_code")
            .add("code", authorizationCode)
            .add("redirect_uri", "meuapp://callback")
            .add("client_id", "9cde7198eaf54c06860b6d0257dcd893")
            .add("client_secret", "d601127a963c4791a61e9145bedd7fe6")
            .build()

        val request = Request.Builder()
            .url("https://accounts.spotify.com/api/token")
            .post(requestBody)
            .build()

        val response = withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }

        if (!response.isSuccessful) {
            throw IOException("Erro: ${response.code} - ${response.body?.string()}")
        }

        val responseBody = response.body?.string()
        val jsonObject = JSONObject(responseBody)
        val accessToken = jsonObject.getString("access_token")
        val refreshToken = jsonObject.getString("refresh_token")
        return Tokens(accessToken, refreshToken)
    }

    override suspend fun refreshAccessToken(refreshToken: String): Tokens {
        val requestBody = FormBody.Builder()
            .add("grant_type", "refresh_token")
            .add("refresh_token", refreshToken)
            .add("client_id", "9cde7198eaf54c06860b6d0257dcd893")
            .add("client_secret", "d601127a963c4791a61e9145bedd7fe6")
            .build()

        val request = Request.Builder()
            .url("https://accounts.spotify.com/api/token")
            .post(requestBody)
            .build()

        val response = withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }

        if (!response.isSuccessful) {
            throw IOException("Erro: ${response.code} - ${response.body?.string()}")
        }

        val responseBody = response.body?.string()
        val jsonObject = JSONObject(responseBody)
        val accessToken = jsonObject.getString("access_token")
        val newRefreshToken = jsonObject.optString("refresh_token", refreshToken)
        return Tokens(accessToken, newRefreshToken)
    }

    override fun saveTokens(accessToken: String, refreshToken: String) {
        val editor = sharedPreferences.edit()
        editor.putString("ACCESS_TOKEN", accessToken)
        editor.putString("REFRESH_TOKEN", refreshToken)
        editor.apply()
    }

    override fun loadTokens(): Pair<String, String> {
        val accessToken = sharedPreferences.getString("ACCESS_TOKEN", "") ?: ""
        val refreshToken = sharedPreferences.getString("REFRESH_TOKEN", "") ?: ""
        return Pair(accessToken, refreshToken)
    }
}
