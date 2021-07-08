package com.benrostudios.enchante.ui.nearby.fragments

import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.benrostudios.enchante.R
import com.benrostudios.enchante.databinding.FragmentNearbyTransactionDetailsBinding
import com.benrostudios.enchante.databinding.FragmentWalletBinding
import com.benrostudios.enchante.ui.viewmodels.NearbyViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class NearbyTransactionDetailsFragment : Fragment() {
    private var _binding: FragmentNearbyTransactionDetailsBinding? = null
    private val binding get() = _binding!!
    private val nearbyViewModel: NearbyViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNearbyTransactionDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.nearbyDetailsProceedToPaymentBtn.setOnClickListener {
            nearbyViewModel.setAmount(binding.nearbyDetailsMoneyText.text.toString().toDouble())
            d("trial", "chekc ${binding.nearbyDetailsMoneyText.text.toString().toDouble()}")
            this.findNavController()
                .navigate(R.id.action_nearbyTransactionDetailsFragment_to_nearbyTransactionProgressFragment)
        }
    }
}