package com.example.spotify.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.spotify.databinding.FragmentArtistsBinding

class ArtistsFragment : Fragment() {

    private lateinit var binding: FragmentArtistsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArtistsBinding.inflate(inflater, container, false)
        return binding.root
    }
}