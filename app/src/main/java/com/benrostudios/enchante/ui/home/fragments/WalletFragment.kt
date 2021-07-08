package com.benrostudios.enchante.ui.home.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.benrostudios.enchante.R
import com.benrostudios.enchante.databinding.FragmentWalletBinding
import com.benrostudios.enchante.ui.ar.ArPaymentActivity
import com.benrostudios.enchante.ui.nearby.NearbyPaymentActivity
import com.benrostudios.enchante.ui.viewmodels.ApiViewModel
import com.benrostudios.enchante.ui.viewmodels.WalletViewModel
import okhttp3.internal.format
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DecimalFormat


class WalletFragment : Fragment() {

    private var _binding: FragmentWalletBinding? = null
    private val binding get() = _binding!!
    private val walletViewModel: WalletViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.sendFundsCard.setOnClickListener {
            startActivity(Intent(context, NearbyPaymentActivity::class.java))
        }
        getWalletDetails()
        binding.addMoneyCard.setOnClickListener {
//            transferMoney()
            makePurchase()
//            this.findNavController().navigate(R.id.action_walletFragment_to_addFundsFragment)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun getWalletDetails() {
        walletViewModel.getWalletAccount()
        walletViewModel.response.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.walletBalanceText.text =
                    "${DecimalFormat("#,###.##").format(it.balance)} USD"
            }
        })
    }

    private fun transferMoney() {
        walletViewModel.makeTransaction("+13716784041", 2.0)
    }

    private fun makePurchase() {
        walletViewModel.makePurchase()
    }


    companion object {
        const val TAG = "wallet_fragment"
    }
}