package com.benrostudios.enchante.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.benrostudios.enchante.R
import com.benrostudios.enchante.databinding.FragmentOnboardingBinding
import com.benrostudios.enchante.ui.auth.AuthActivity
import com.benrostudios.enchante.ui.home.HomeActivity
import com.benrostudios.enchante.utils.SharedPrefManager
import com.benrostudios.enchante.utils.show
import com.bumptech.glide.Glide
import org.koin.android.ext.android.inject


class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!
    private val sharedPrefManager: SharedPrefManager by inject()

    lateinit var image: String
    lateinit var heading: String
    lateinit var headingDesc: String
    lateinit var desc: String
    var final: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        image = arguments?.get("image").toString()
        heading = arguments?.get("heading").toString()
        headingDesc = arguments?.get("headingDesc").toString()
        desc = arguments?.get("desc").toString()
        final = arguments?.getBoolean("final") ?: false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.onboardingHeading.text = heading
        binding.onboardingHeadingDesc.text = headingDesc
        binding.onboardingDesc.text = desc

        val renderDrawable = when (image) {
            "1" -> R.drawable.ob1
            "2" -> R.drawable.ob2
            "3" -> R.drawable.ob3
            else -> R.drawable.ob4
        }
        when (image) {
            "1" -> binding.onboardingHeading.setTextColor(requireContext().getColor(R.color.light_blue_A200))
            "2" -> binding.onboardingHeading.setTextColor(requireContext().getColor(R.color.primary_orange))
            "3" -> binding.onboardingHeading.setTextColor(requireContext().getColor(R.color.primary_green))
            else -> binding.onboardingHeading.setTextColor(requireContext().getColor(R.color.primary_red))
        }
        Glide.with(this).load(renderDrawable).into(binding.onboardingImage)

        if (final) {
            binding.onboardingNext.setOnClickListener {
                sharedPrefManager.firstOpen = false
                startActivity(Intent(context, AuthActivity::class.java))
                activity?.finish()
            }
            binding.onboardingNext.show()
        }
    }
}