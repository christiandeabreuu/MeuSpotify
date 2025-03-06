package com.example.spotify

import android.os.Build
import androidx.annotation.RequiresApi
import java.security.MessageDigest
import java.util.Base64

object PKCEUtils {

    // Gera um code_verifier aleatório
    fun generateCodeVerifier(): String {
        val allowedChars = ('a'..'z') + ('A'..'Z') + ('0'..'9') + '-' + '.' + '_' + '~'
        return (1..128)
            .map { allowedChars.random() }
            .joinToString("")
    }

    // Gera o code_challenge a partir do code_verifier
    @RequiresApi(Build.VERSION_CODES.O)
    fun generateCodeChallenge(codeVerifier: String): String {
        val bytes = codeVerifier.toByteArray(Charsets.US_ASCII)
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest)
    }
}