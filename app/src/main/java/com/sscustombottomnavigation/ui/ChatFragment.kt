package com.sscustombottomnavigation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sscustombottomnavigation.R
import com.sscustombottomnavigation.databinding.FragmentChatBinding
import com.sscustombottomnavigation.databinding.FragmentFavoriteBinding

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::binding.isInitialized) {
            binding = FragmentChatBinding.inflate(inflater, container, false)
        }
        return binding.root
    }
}