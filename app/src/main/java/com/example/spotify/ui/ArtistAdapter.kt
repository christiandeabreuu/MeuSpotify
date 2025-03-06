//package com.example.spotify.ui
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.spotify.Artista
//
//class ArtistaAdapter(
//    private val artistas: List<Artista>
//) : RecyclerView.Adapter<ArtistaAdapter.ArtistaViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistaViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_artista, parent, false)
//        return ArtistaViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ArtistaViewHolder, position: Int) {
//        val artista = artistas[position]
//        holder.bind(artista)
//    }
//
//    override fun getItemCount(): Int = artistas.size
//
//    class ArtistaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val artistaNomeTextView: TextView = itemView.findViewById(R.id.artistaNomeTextView)
//        private val artistaImageView: ImageView = itemView.findViewById(R.id.artistaImageView)
//
//        fun bind(artista: Artista) {
//            artistaNomeTextView.text = artista.nome
//            // Usando Glide para carregar a imagem do artista
//            Glide.with(itemView.context)
//                .load(artista.imagemUrl)
//                .into(artistaImageView)
//        }
//    }
//}