package com.benrostudios.enchante.ui.home.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.benrostudios.enchante.databinding.FragmentHomeBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inflateUser()
    }

    @SuppressLint("SetTextI18n")
    private fun inflateUser() {
        Glide.with(this).load(Firebase.auth.currentUser?.photoUrl).circleCrop()
            .into(binding.homeUserPorfileImage)
        binding.homeUsername.text = "Hello, ${Firebase.auth.currentUser?.displayName ?: "User"}"
    }
}