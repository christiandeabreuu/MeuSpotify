package com.example.spotify.ui.artist

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import android.util.Log
import com.example.spotify.R
import com.example.spotify.data.local.ArtistWithImages
import com.example.spotify.databinding.ItemArtistaBinding
import com.example.spotify.ui.albuns.AlbumsActivity

class ArtistAdapter(
    private val context: Context,
    private val accessToken: String,
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
                val intent = Intent(context, AlbumsActivity::class.java).apply {
                    putExtra("ARTIST_ID", artist.id)
                    putExtra("ACCESS_TOKEN", accessToken)
                    putExtra("ARTIST", artist.name)
                    putExtra("IMAGE_URL", firstImage?.url)
                }
                context.startActivity(intent)
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