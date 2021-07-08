package com.benrostudios.enchante.ui.home.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.benrostudios.enchante.R
import com.benrostudios.enchante.adapters.withSimpleAdapter
import com.benrostudios.enchante.databinding.FragmentWalletBinding
import com.benrostudios.enchante.ui.nearby.NearbyPaymentActivity
import com.benrostudios.enchante.ui.viewmodels.WalletViewModel
import com.bumptech.glide.Glide
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
            this.findNavController().navigate(R.id.action_walletFragment_to_addFundsFragment)
        }
        binding.offersRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.offersRv.withSimpleAdapter(listOf("Free event pass"), R.layout.offer_item) {
            itemView.findViewById<TextView>(R.id.offer_name).text = "Free event pass"
            itemView.findViewById<TextView>(R.id.offer_token).text = "Get this for $10USD"
            Glide.with(itemView).load(R.drawable.offer)
                .into(itemView.findViewById<ImageView>(R.id.offer_image))
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