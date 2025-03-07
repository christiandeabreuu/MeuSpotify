package com.example.spotify.ui.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.spotify.R
import com.example.spotify.data.model.Playlist
import com.example.spotify.databinding.ItemPlaylistBinding

class PlaylistAdapter : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    private var playlists: List<Playlist> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
    }

    override fun getItemCount(): Int = playlists.size

    fun submitList(newPlaylists: List<Playlist>) {
        playlists = newPlaylists
        notifyDataSetChanged()
    }

    class PlaylistViewHolder(private val binding: ItemPlaylistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(playlist: Playlist) {
            binding.playlistNameTextView.text = playlist.name
            val image = playlist.images?.firstOrNull()
            if (image != null) {
                binding.playlistImageView.load(image.url) {
                }
            } else {
                binding.playlistImageView.setImageResource(R.drawable.icon_spotify)
            }
        }
    }
}
