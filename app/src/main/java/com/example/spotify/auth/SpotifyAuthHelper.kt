package com.example.spotify.auth

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import okhttp3.*
import java.io.IOException

class SpotifyAuthHelper(private val context: Context) {

    private val CLIENT_ID = "9cde7198eaf54c06860b6d0257dcd893"
    private val CLIENT_SECRET = "d601127a963c4791a61e9145bedd7fe6"

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAccessToken(onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val client = OkHttpClient()

        // Corpo da requisição
        val requestBody = FormBody.Builder()
            .add("grant_type", "client_credentials")
            .build()

        // Codificar CLIENT_ID e CLIENT_SECRET em Base64
        val credentials = "$CLIENT_ID:$CLIENT_SECRET"
        val authHeader = "Basic " + java.util.Base64.getEncoder().encodeToString(credentials.toByteArray())

        // Requisição para obter o token
        val request = Request.Builder()
            .url("https://accounts.spotify.com/api/token")
            .post(requestBody)
            .header("Authorization", authHeader)
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
                    Log.d("SpotifyAuthHelper", "Resposta da API: $responseBody")
                    val accessToken = responseBody?.let { parseAccessToken(it) }
                    if (accessToken != null) {
                        Log.d("SpotifyAuthHelper", "Token de acesso obtido: $accessToken")
                        onSuccess(accessToken)
                    } else {
                        Log.e("SpotifyAuthHelper", "Token de acesso não encontrado na resposta")
                        onError("Token de acesso não encontrado na resposta")
                    }
                } else {
                    Log.e("SpotifyAuthHelper", "Erro ao obter token: ${response.code}")
                    onError("Erro ao obter token: ${response.code}")
                }
            }
        })
    }

    private fun parseAccessToken(responseBody: String): String? {
        return try {
            val jsonObject = org.json.JSONObject(responseBody)
            jsonObject.getString("access_token")
        } catch (e: Exception) {
            Log.e("SpotifyAuthHelper", "Erro ao analisar JSON", e)
            null
        }
    }
}