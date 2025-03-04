package com.example.spotify.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.spotify.R
import com.example.spotify.auth.SpotifyAuthHelper
import com.spotify.sdk.android.auth.AccountsQueryParameters.CLIENT_ID
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse

import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.spotify.sdk.android.auth.AccountsQueryParameters.REDIRECT_URI


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var spotifyAuthHelper: SpotifyAuthHelper
    private lateinit var webView: WebView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        spotifyAuthHelper = SpotifyAuthHelper(this)
        webView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null && url.startsWith(REDIRECT_URI)) {
                    // Handle the redirect URI here
                    handleRedirectUri(url)
                    return true
                }
                return false
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                // Handle the error here
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Perform actions after the page is finished loading
            }
        }
        webView.loadUrl("http://www.example.com")

        val REQUEST_CODE = 1337
        val REDIRECT_URI = "meuapp://callback"

        val builder = AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.CODE, REDIRECT_URI)
        builder.setScopes(arrayOf("user-read-private", "user-read-email", "streaming")) // Adicione mais scopes se necessário
        val request = builder.build()

        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1337) {
            val response = AuthorizationClient.getResponse(resultCode, data)
            Log.d("LoginActivity", "Response Type: ${response.type}")
            when (response.type) {
                AuthorizationResponse.Type.CODE -> {
                    val authorizationCode = response.code
                    Log.d("LoginActivity", "Código de autorização recebido: $authorizationCode")
                    CoroutineScope(Dispatchers.IO).launch {
                        spotifyAuthHelper.getAccessToken(authorizationCode, { token ->
                            Log.d("LoginActivity", "Token de acesso obtido: $token")
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("ACCESS_TOKEN", token)
                            startActivity(intent)
                        }, { error ->
                            Log.e("LoginActivity", "Erro ao obter token de acesso: $error")
                            runOnUiThread {
                                Toast.makeText(this@LoginActivity, "Erro: $error", Toast.LENGTH_LONG).show()
                            }
                        })
                    }
                }
                AuthorizationResponse.Type.ERROR -> {
                    Log.e("LoginActivity", "Erro na autorização: ${response.error}")
                    Toast.makeText(this, "Erro: ${response.error}", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Log.e("LoginActivity", "Tipo de resposta desconhecido: ${response.type}")
                    // Outro tipo de resposta, se necessário
                }
            }
        }
    }

    private fun handleRedirectUri(uri: String) {
        // Implement your redirect URI handling logic here
    }
}



        // Iniciar autenticação
//        Log.d("LoginActivity", "Iniciando autenticação...")
//        spotifyAuthHelper.getAccessToken(
//            onSuccess = { accessToken ->
//                Log.d("LoginActivity", "Token de acesso obtido com sucesso: $accessToken")
//                // Salvando token
//                val sharedPrefs = getSharedPreferences("SPOTIFY", Context.MODE_PRIVATE)
//                with(sharedPrefs.edit()) {
//                    putString("ACCESS_TOKEN", accessToken)
//                    apply()
//                }
//
//                // Navegação para MainActivity
//                val intent = Intent(this, MainActivity::class.java)
//                intent.putExtra("ACCESS_TOKEN", accessToken)
//                startActivity(intent)
//                finish()
//            },
//            onError = { error ->
//                Log.e("LoginActivity", "Erro de autenticação: $error")
//                Toast.makeText(this, "Erro de autenticação: $error", Toast.LENGTH_SHORT).show()
//            }
//        )
