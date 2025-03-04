package com.example.spotify.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spotify.R
import com.example.spotify.RetrofitInstance
import com.example.spotify.UserProfile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private lateinit var accessToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        accessToken = intent.getStringExtra("ACCESS_TOKEN")!!

        getUserProfile()
    }

    private fun getUserProfile() {
        val api = RetrofitInstance.api
        val call = api.getUserProfile("Bearer $accessToken")

        call.enqueue(object : retrofit2.Callback<UserProfile> {
            override fun onResponse(call: Call<UserProfile>, response: retrofit2.Response<UserProfile>) {
                if (response.isSuccessful) {
                    val userProfile = response.body()
                    // Faça algo com os dados do perfil do usuário
                    Toast.makeText(this@MainActivity, "Bem-vindo, ${userProfile?.displayName}", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@MainActivity, "Falha ao obter perfil", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Erro: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}



