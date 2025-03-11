package com.example.spotify.ui.artist

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.spotify.R
import com.example.spotify.data.local.ArtistWithImages
import com.example.spotify.databinding.ItemArtistaBinding

class ArtistAdapter(
    private val accessToken: String,
    private val onClick: (Artist) -> Unit
) : PagingDataAdapter<ArtistWithImages, ArtistAdapter.ArtistViewHolder>(ArtistDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val binding = ItemArtistaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        Log.d("ArtistAdapter", "onBindViewHolder() chamado com position: $position")
        val artistWithImages = getItem(position)
        Log.d("ArtistAdapter", "Artist at position $position: $artistWithImages")
        artistWithImages?.let {
            val artist = it.artist
            val firstImage = it.images.firstOrNull()
            holder.binding.tvArtist.text = artist.name
            holder.binding.imageArtist.load(firstImage?.url) {
                transformations(coil.transform.CircleCropTransformation())
                placeholder(R.drawable.ic_spotify_full)
                error(R.drawable.ic_spotify_full)
            }
            holder.binding.root.setOnClickListener {
                onClick(artist)
            }
        }
    }

    class ArtistViewHolder(val binding: ItemArtistaBinding) : RecyclerView.ViewHolder(binding.root)

    class ArtistDiffCallback : DiffUtil.ItemCallback<ArtistWithImages>() {
        override fun areItemsTheSame(oldItem: ArtistWithImages, newItem: ArtistWithImages): Boolean {
            return oldItem.artist.id == newItem.artist.id
        }

        override fun areContentsTheSame(oldItem: ArtistWithImages, newItem: ArtistWithImages): Boolean {
            return oldItem == newItem
        }
    }
}