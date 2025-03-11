package com.example.spotify.ui.artist

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.spotify.R
import com.example.spotify.databinding.ItemArtistaBinding

class ArtistAdapter(
    private val accessToken: String,
    private val onClick: (com.example.spotify.data.model.Artist) -> Unit
) : PagingDataAdapter<com.example.spotify.data.model.Artist, ArtistAdapter.ArtistViewHolder>(
    ArtistDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val binding = ItemArtistaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = getItem(position)

        Log.d("ArtistAdapter", "Artist at position $position: $artist")
        artist?.let {
            holder.binding.tvArtist.text = it.name
            holder.binding.imageArtist.load(it.images.firstOrNull()?.url) {
                transformations(coil.transform.CircleCropTransformation())
                placeholder(R.drawable.ic_spotify_full)
                error(R.drawable.ic_spotify_full)
            }
            Log.d("ArtistAdapter", "Token ao criar o Intent: $accessToken")
            holder.binding.root.setOnClickListener {
                onClick(artist)
            }
        }
    }

    class ArtistViewHolder(val binding: ItemArtistaBinding) : RecyclerView.ViewHolder(binding.root)

    class ArtistDiffCallback : DiffUtil.ItemCallback<com.example.spotify.data.model.Artist>() {
        override fun areItemsTheSame(
            oldItem: com.example.spotify.data.model.Artist,
            newItem: com.example.spotify.data.model.Artist
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: com.example.spotify.data.model.Artist,
            newItem: com.example.spotify.data.model.Artist
        ): Boolean {
            return oldItem == newItem
        }
    }
}