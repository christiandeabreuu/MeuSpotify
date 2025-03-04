package com.example.spotify.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class SpotifyAuthHelper(private val context: Context) {

    private val CLIENT_ID = "9cde7198eaf54c06860b6d0257dcd893"
    private val CLIENT_SECRET = "d601127a963c4791a61e9145bedd7fe6"
    private val REDIRECT_URI = "meuapp://callback"
    private val authUrl =
        "https://accounts.spotify.com/authorize?client_id=$CLIENT_ID&response_type=code&redirect_uri=$REDIRECT_URI&scope=user-top-read"
    private val TOKEN_URL = "https://accounts.spotify.com/api/token"

    fun getAuthUrl(): String {
        return authUrl
    }

    fun redirectToLogin() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getAuthUrl()))
        context.startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAccessToken(
        code: String,
        onSuccess: (String, String) -> Unit,
        onError: (String) -> Unit
    ) {
        val client = OkHttpClient()

        // Corpo da requisição
        val requestBody = FormBody.Builder()
            .add("grant_type", "authorization_code")
            .add("code", code)
            .add("redirect_uri", REDIRECT_URI)
            .add("client_id", CLIENT_ID)
            .add("client_secret", CLIENT_SECRET)
            .build()

        // Requisição para obter o token
        val request = Request.Builder()
            .url(TOKEN_URL)
            .post(requestBody)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .build()

        Log.d("SpotifyAuthHelper", "Fazendo requisição para obter token de acesso...")
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("SpotifyAuthHelper", "Falha na requisição: ${e.message}")
                onError("Falha na requisição: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        try {
                            val jsonObject = org.json.JSONObject(responseBody)
                            val accessToken = jsonObject.getString("access_token")
                            val refreshToken = jsonObject.getString("refresh_token")
                            onSuccess(accessToken, refreshToken)
                        } catch (e: Exception) {
                            Log.e("SpotifyAuthHelper", "Erro ao processar resposta: ${e.message}")
                            onError("Erro ao processar resposta: ${e.message}")
                        }
                    } else {
                        Log.e("SpotifyAuthHelper", "Resposta vazia")
                        onError("Resposta vazia")
                    }
                } else {
                    Log.e("SpotifyAuthHelper", "Erro ao obter token: ${response.code}")
                    onError("Erro ao obter token: ${response.code}")
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshAccessToken(
        refreshToken: String,
        onSuccess: (String, String) -> Unit, // Agora retorna accessToken e refreshToken
        onError: (String) -> Unit
    ){
        val client = OkHttpClient()

        // Corpo da requisição
        val requestBody = FormBody.Builder()
            .add("grant_type", "refresh_token")
            .add("refresh_token", refreshToken)
            .add("client_id", CLIENT_ID)
            .add("client_secret", CLIENT_SECRET)
            .build()

        // Requisição para obter o novo token
        val request = Request.Builder()
            .url(TOKEN_URL)
            .post(requestBody)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .build()

        Log.d("SpotifyAuthHelper", "Fazendo requisição para obter novo token de acesso...")
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("SpotifyAuthHelper", "Falha na requisição: ${e.message}")
                onError("Falha na requisição: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        try {
                            val jsonObject = org.json.JSONObject(responseBody)
                            val accessToken = jsonObject.getString("access_token")
                            val refreshToken = jsonObject.optString("refresh_token") // Pode ser nulo
                            if (refreshToken.isNotEmpty()) {
                                onSuccess(accessToken, refreshToken) // Retorna ambos
                            } else {
                                onSuccess(accessToken, "") // Retorna apenas o access token
                            }
                        } catch (e: Exception) {
                            Log.e("SpotifyAuthHelper", "Erro ao processar resposta: ${e.message}")
                            onError("Erro ao processar resposta: ${e.message}")
                        }
                    } else {
                        Log.e("SpotifyAuthHelper", "Resposta vazia")
                        onError("Resposta vazia")
                    }
                } else {
                    Log.e("SpotifyAuthHelper", "Erro ao obter token: ${response.code}")
                    onError("Erro ao obter token: ${response.code}")
                }
            }
        })
    }
}


