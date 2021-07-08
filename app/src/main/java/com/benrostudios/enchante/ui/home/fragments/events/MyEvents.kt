package com.benrostudios.enchante.ui.home.fragments.events

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.benrostudios.enchante.R
import com.benrostudios.enchante.adapters.withSimpleAdapter
import com.benrostudios.enchante.databinding.FragmentHomeBinding
import com.benrostudios.enchante.databinding.FragmentMyEventsBinding
import com.benrostudios.enchante.ui.viewmodels.ApiViewModel
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel


class MyEvents : Fragment() {

    private var _binding: FragmentMyEventsBinding? = null
    private val binding get() = _binding!!

    private val apiViewModel: ApiViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.eventsRv.layoutManager = LinearLayoutManager(requireContext())
        getMyEvents()
    }

    private fun getMyEvents(){
        apiViewModel.getMyEventsRoute()
        apiViewModel.eventsResponse.observe(viewLifecycleOwner, Observer {
            it.let {
                binding.eventsRv.withSimpleAdapter(
                    it.data.pastEvents!!,
                    R.layout.event_item
                ) { data ->
                    itemView.findViewById<TextView>(R.id.event_item_title).text =
                        data.eventId.name
                    itemView.findViewById<TextView>(R.id.event_item_pass_type).text =
                        data.eventId.passes?.get(0)?.passName
                    itemView.findViewById<TextView>(R.id.event_item_location).text =
                        data.eventId.location?.humanformAddress ?: "USA"
                    Glide.with(itemView).load(data.eventId.assets?.banner)
                        .into(itemView.findViewById(R.id.event_item_image))
                }
            }
        })
    }
}