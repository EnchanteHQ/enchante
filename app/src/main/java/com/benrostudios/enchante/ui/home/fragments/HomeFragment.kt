package com.benrostudios.enchante.ui.home.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.benrostudios.enchante.R
import com.benrostudios.enchante.adapters.withSimpleAdapter
import com.benrostudios.enchante.databinding.FragmentHomeBinding
import com.benrostudios.enchante.ui.home.HomeActivity
import com.benrostudios.enchante.ui.viewmodels.ApiViewModel
import com.benrostudios.enchante.ui.viewmodels.AuthViewModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val apiViewModel: ApiViewModel by viewModel()


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
        binding.rvPopularEvents.layoutManager = LinearLayoutManager(requireContext())
        inflateUser()
        callHomeRoute()
    }

    @SuppressLint("SetTextI18n")
    private fun inflateUser() {
        Glide.with(this).load(Firebase.auth.currentUser?.photoUrl).circleCrop()
            .into(binding.homeUserPorfileImage)
        binding.homeUsername.text = "Hello, ${Firebase.auth.currentUser?.displayName ?: "User"}"
    }

    private fun callHomeRoute() {
        apiViewModel.homeRoute(23.20172052330953,72.598053237886)
        apiViewModel.response.observe(viewLifecycleOwner,  Observer {
            if(it.data.eventsInRange?.isNotEmpty() == true){
                binding.tipDesc.text = it.data.tip?.description
                binding.tipTitle.text = it.data.tip?.title

                binding.rvPopularEvents.withSimpleAdapter(it.data.eventsInRange, R.layout.home_event_item){data ->
                    itemView.findViewById<TextView>(R.id.home_event_item_title).text = data.event.name
                    itemView.findViewById<TextView>(R.id.home_event_item_status).text = data.event.status
                    itemView.findViewById<TextView>(R.id.home_event_item_location).text = data.event.location?.humanformAddress ?: "USA"
                    itemView.findViewById<TextView>(R.id.home_event_item_seats_left).text = "${data.availableSeats}"
                    Glide.with(itemView).load(data.event.assets?.banner).into(itemView.findViewById(R.id.home_event_item_image))
                }
            }
        })
    }
}