package com.example.spotify.ui.artist

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.spotify.R
import com.example.spotify.data.model.Artist
import com.example.spotify.databinding.ItemArtistaBinding

class ArtistAdapter(
    private val accessToken: String, private val onClick: (Artist) -> Unit
) : PagingDataAdapter<Artist, ArtistAdapter.ArtistViewHolder>(ArtistDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val binding = ItemArtistaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = getItem(position)

        Log.d("ArtistAdapter", "Artist at position $position: $artist")
        artist?.let {
            holder.binding.tvArtist.text = it.name
            Log.d("ArtistAdapter", "Image URL: ${artist.images.firstOrNull()?.url}")
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

    class ArtistDiffCallback : DiffUtil.ItemCallback<Artist>() {
        override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean {
            return oldItem == newItem
        }
    }
}