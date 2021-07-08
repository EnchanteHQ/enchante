package com.benrostudios.enchante.ui.nearby.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.benrostudios.enchante.R
import com.benrostudios.enchante.databinding.FragmentNearbyTransactionDetailsBinding
import com.benrostudios.enchante.databinding.FragmentNearbyTransactionResultBinding

class NearbyTransactionResultFragment : Fragment() {

    private var _binding: FragmentNearbyTransactionResultBinding? = null
    private val binding get() = _binding!!
    private var okResult = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        okResult = arguments?.getBoolean("okResult") ?: false
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNearbyTransactionResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nearbyResultBackButton.setOnClickListener {
            activity?.finish()
        }
    }
}