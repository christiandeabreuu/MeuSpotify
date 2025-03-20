package com.example.spotify

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import java.io.File

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Configura o ImageLoader com cache de memória e cache de disco
        val imageLoader = ImageLoader.Builder(applicationContext)
            .crossfade(true)
            .memoryCache {
                MemoryCache.Builder(applicationContext)
                    .maxSizePercent(0.25) // Usa 25% da memória disponível para cache
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(File(applicationContext.cacheDir, "image_cache"))
                    .maxSizeBytes(50L * 1024 * 1024) // 50MB de cache de disco
                    .build()
            }
            .build()

        // Define o ImageLoader global do Coil para toda a aplicação
        Coil.setImageLoader(imageLoader)
    }
}
