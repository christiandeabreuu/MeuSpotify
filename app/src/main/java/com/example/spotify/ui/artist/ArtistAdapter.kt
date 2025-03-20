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

        artist?.let {
            Log.d("DebugAdapter", "Artista recebido: ${it.name} | ID: ${it.id} | Imagens: ${it.images.size}")
            it.images.forEach { image ->
                Log.d("DebugAdapter", "URL da Imagem: ${image.url}")
            }

            // Verificar se há URL válida para carregar
            val imageUrl = it.images.firstOrNull()?.url
            if (imageUrl.isNullOrEmpty()) {
                Log.e("DebugAdapter", "Nenhuma URL válida para o artista: ${it.name}")
                holder.binding.imageArtist.setImageResource(R.drawable.ic_spotify_full)
            } else {
                holder.binding.imageArtist.load(imageUrl) {
                    transformations(coil.transform.CircleCropTransformation())
                    placeholder(R.drawable.ic_spotify_full)
                    error(R.drawable.ic_spotify_full)
                }
            }

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