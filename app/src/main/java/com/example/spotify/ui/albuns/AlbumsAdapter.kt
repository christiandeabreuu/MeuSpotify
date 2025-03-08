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

    // Função para converter a data de "yyyy-MM-dd" para "dd/MM/yyyy"
    private fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }
}
