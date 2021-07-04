package com.benrostudios.enchante.ui.home.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.benrostudios.enchante.R
import com.benrostudios.enchante.adapters.ViewPagerAdapter
import com.benrostudios.enchante.databinding.FragmentEventsBinding
import com.benrostudios.enchante.ui.home.fragments.events.MyEvents
import com.google.android.material.tabs.TabLayoutMediator


class EventsFragment : Fragment() {

    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragments = mutableListOf<Fragment>()
        fragments.add(MyEvents())
        fragments.add(MyEvents())
        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle, fragments)
        binding.eventsViewPager.adapter = adapter
        val titles = mutableListOf("Upcoming Events", "Past Events")
        TabLayoutMediator(binding.eventsTab, binding.eventsViewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}