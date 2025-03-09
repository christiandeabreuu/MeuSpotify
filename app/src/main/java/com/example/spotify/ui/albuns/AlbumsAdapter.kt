package com.example.spotify.ui.albuns

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.spotify.data.model.Album
import com.example.spotify.databinding.ItemAlbumBinding
import java.text.SimpleDateFormat
import java.util.Locale

class AlbumsAdapter(private var albums: List<Album>) : RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = albums[position]
        holder.binding.albumName.text = album.name
        holder.binding.albumReleaseDate.text = formatDate(album.releaseDate)
        holder.binding.albumCover.load(album.images.firstOrNull()?.url)
    }

    override fun getItemCount(): Int = albums.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newAlbums: List<Album>) {
        albums = newAlbums
        notifyDataSetChanged()
    }

    class AlbumViewHolder(val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root)

    fun formatDate(dateString: String): String {
        val formats = listOf(
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()), // Formato completo: ano/mês/dia
            SimpleDateFormat("MM/yyyy", Locale.getDefault()),    // Formato mês/ano
            SimpleDateFormat("yyyy", Locale.getDefault())        // Formato apenas ano
        )

        val targetFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Formato final desejado

        for (format in formats) {
            try {
                val parsedDate = format.parse(dateString)
                if (parsedDate != null) {
                    return targetFormat.format(parsedDate) // Retorna no formato dd/MM/yyyy
                }
            } catch (e: Exception) {
                // Ignorar e tentar o próximo formato
            }
        }

        // Caso nenhuma correspondência seja encontrada, retornar uma data padrão
        return "01/01/1989" // Adicione aqui um valor padrão apropriado
    }

}
