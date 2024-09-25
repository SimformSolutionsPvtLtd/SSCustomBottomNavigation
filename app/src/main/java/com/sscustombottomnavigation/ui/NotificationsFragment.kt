package com.sscustombottomnavigation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sscustombottomnavigation.R
import com.sscustombottomnavigation.databinding.FragmentChatBinding
import com.sscustombottomnavigation.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::binding.isInitialized) {
            binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        }
        return binding.root
    }
}