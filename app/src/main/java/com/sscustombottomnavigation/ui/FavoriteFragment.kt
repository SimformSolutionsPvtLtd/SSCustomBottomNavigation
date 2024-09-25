package com.sscustombottomnavigation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sscustombottomnavigation.R
import com.sscustombottomnavigation.databinding.FragmentFavoriteBinding
import com.sscustombottomnavigation.databinding.FragmentHomeBinding

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::binding.isInitialized) {
            binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        }
        return binding.root
    }
}