    package com.example.spotify.auth

    import android.content.Context
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.withContext
    import okhttp3.FormBody
    import okhttp3.OkHttpClient
    import okhttp3.Request
    import org.json.JSONObject
    import java.io.IOException
    import java.util.concurrent.TimeUnit


    class SpotifyAuthHelper(private val context: Context) {

        private val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        // Função para obter o access token usando o authorization code
        suspend fun getAccessToken(authorizationCode: String): Tokens {
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

        // Função para renovar o access token usando o refresh token
        suspend fun refreshAccessToken(refreshToken: String): Tokens {
            val requestBody = FormBody.Builder()
                .add("grant_type", "refresh_token")
                .add("refresh_token", refreshToken)
                .add("client_id", "9cde7198eaf54c06860b6d0257dcd893") // Substitua pelo seu Client ID
                .add("client_secret", "d601127a963c4791a61e9145bedd7fe6") // Substitua pelo seu Client Secret
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
            val newRefreshToken = jsonObject.optString("refresh_token", refreshToken) // Mantém o refreshToken atual se não for fornecido um novo
            return Tokens(accessToken, newRefreshToken)
        }

        // Classe de dados para armazenar os tokens
        data class Tokens(val accessToken: String, val refreshToken: String)
    }
