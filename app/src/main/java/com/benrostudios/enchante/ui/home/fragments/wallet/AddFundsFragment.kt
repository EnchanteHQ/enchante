package com.benrostudios.enchante.ui.home.fragments.wallet

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.benrostudios.enchante.R
import com.benrostudios.enchante.SplashActivity
import com.benrostudios.enchante.databinding.FragmentAddFundsBinding
import com.benrostudios.enchante.databinding.FragmentWalletBinding
import com.benrostudios.enchante.ui.auth.AuthActivity
import com.benrostudios.enchante.ui.home.HomeActivity
import com.benrostudios.enchante.ui.onboarding.OnboardingActivity
import com.benrostudios.enchante.ui.viewmodels.WalletViewModel
import com.benrostudios.enchante.utils.hide
import com.benrostudios.enchante.utils.show
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddFundsFragment : Fragment() {

    private var _binding: FragmentAddFundsBinding? = null
    private val binding get() = _binding!!
    private val walletViewModel: WalletViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddFundsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addFundsButton.setOnClickListener {
            if (binding.addFundsInput.text.toString().toDouble() >= 0) {
                addFunds()
                binding.addFundsButton.hide()
                binding.paymentPb.show()
            }
        }
    }

    private fun addFunds() {
        walletViewModel.addFunds(binding.addFundsInput.text.toString().toDouble())
        Handler(Looper.getMainLooper()).postDelayed(
            {
                this.findNavController().popBackStack()
            }, 1500L
        )
    }
}