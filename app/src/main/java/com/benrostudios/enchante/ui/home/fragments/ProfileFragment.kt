package com.benrostudios.enchante.ui.home.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.benrostudios.enchante.R
import com.benrostudios.enchante.databinding.FragmentHomeBinding
import com.benrostudios.enchante.databinding.FragmentProfileBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        Glide.with(this).load(Firebase.auth.currentUser?.photoUrl).circleCrop()
            .into(binding.profileImage)
        binding.profileUsername.text = Firebase.auth.currentUser?.displayName
    }
}