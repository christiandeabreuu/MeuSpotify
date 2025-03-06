package com.example.spotify.ui.artist

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.spotify.data.Artist
import com.example.spotify.databinding.ItemArtistaBinding
import com.example.spotify.ui.albuns.AlbumsActivity

class ArtistAdapter(
    private val artists: List<Artist>,
    private val context: Context,
    private val accessToken: String
) : RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val binding = ItemArtistaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = artists[position]
        holder.binding.tvArtist.text = artist.name
        holder.binding.imageArtist.load(artist.images.firstOrNull()?.url){
            transformations(coil.transform.CircleCropTransformation())
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

    override fun getItemCount(): Int = artists.size

    class ArtistViewHolder(val binding: ItemArtistaBinding) : RecyclerView.ViewHolder(binding.root)
}
