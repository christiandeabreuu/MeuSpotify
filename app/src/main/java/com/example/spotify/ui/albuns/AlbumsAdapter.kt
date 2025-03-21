package com.example.spotify.ui.albuns

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.spotify.R
import com.example.spotify.data.local.AlbumDB
import com.example.spotify.data.model.Album
import com.example.spotify.databinding.ItemAlbumBinding
import java.text.SimpleDateFormat
import java.util.Locale

class AlbumsAdapter(private var albums: List<AlbumDB>) :
    RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = albums[position]
        holder.bind(album)
    }

    override fun getItemCount() = albums.size

    fun updateData(newAlbums: List<AlbumDB>) {
        albums = newAlbums
        notifyDataSetChanged()
    }

    class AlbumViewHolder(private val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: AlbumDB) {
            binding.albumName.text = album.name
            binding.albumReleaseDate.text = album.artist
            binding.albumCover.load(album.imageUrl) {
                error(R.drawable.ic_spotify_full_black)
            }
        }
    }
}

