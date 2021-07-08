package com.benrostudios.enchante.ui.nearby.fragments

import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.benrostudios.enchante.R
import com.benrostudios.enchante.ui.viewmodels.NearbyViewModel
import org.koin.android.ext.android.inject


class NearbyTransactionProgressFragment : Fragment() {

    private val nearbyViewModel: NearbyViewModel by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nearby_transaction_progress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        d("trial", nearbyViewModel.getAmount().toString())
    }
}