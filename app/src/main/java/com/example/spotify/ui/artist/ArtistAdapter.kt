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
import com.example.spotify.data.model.Artist
import com.example.spotify.databinding.ItemArtistaBinding
import com.example.spotify.ui.albuns.AlbumsActivity

class ArtistAdapter(
    private val context: Context,
    private val accessToken: String,
) : PagingDataAdapter<Artist, ArtistAdapter.ArtistViewHolder>(ArtistDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val binding = ItemArtistaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = getItem(position) // Pega o item da posição com `PagingDataAdapter`
        Log.d("ArtistAdapter", "Artist at position $position: $artist")
        artist?.let { // Apenas executa o bind se o item não for nulo
            holder.binding.tvArtist.text = it.name
            holder.binding.imageArtist.load(it.images.firstOrNull()?.url) {
                transformations(coil.transform.CircleCropTransformation())
                placeholder(R.drawable.ic_spotify_full)
                error(R.drawable.ic_spotify_full)
            }
            holder.binding.root.setOnClickListener {
                val intent = Intent(context, AlbumsActivity::class.java).apply {
                    putExtra("ARTIST_ID", artist.id)
                    putExtra("ACCESS_TOKEN", accessToken)
                    putExtra("ARTIST", artist.name)
                    putExtra("IMAGE_URL", artist.images.firstOrNull()?.url)
                }
                context.startActivity(intent)
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

