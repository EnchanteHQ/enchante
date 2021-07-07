package com.benrostudios.enchante.ui.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.benrostudios.enchante.databinding.ActivityOnboardingBinding
import com.benrostudios.enchante.ui.home.fragments.events.MyEvents


class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val fragments = mutableListOf<Fragment>()
        fragments.add(MyEvents())
        fragments.add(MyEvents())
        val viewPagerAdapter = ViewPagerAdapterOb(supportFragmentManager)
        viewPagerAdapter.addFragment(newInstance("1", "Maps", "We change the way you see events", "aasdf", false))
        viewPagerAdapter.addFragment(newInstance("2", "Payment and  Budget", "AR payments brought to life", "aasdf", false))
        viewPagerAdapter.addFragment(newInstance("3", "Connect", "Connect with like minded people in events.", "aasdf", false))
        viewPagerAdapter.addFragment(newInstance("4", "Currency", "Booking made easy!!!", "aasdf", true))
        binding.onboardingViewPager.adapter = viewPagerAdapter
        binding.tabLayoutIndicator.setupWithViewPager(binding.onboardingViewPager)

    }

    private fun newInstance(
        image: String,
        heading: String,
        headingDesc: String,
        desc: String,
        final: Boolean
    ): OnboardingFragment {
        val args = Bundle()
        args.putString("image", image)
        args.putString("heading", heading)
        args.putString("headingDesc", headingDesc)
        args.putString("desc", desc)
        args.putBoolean("final", final)
        val fragment = OnboardingFragment()
        fragment.arguments = args
        return fragment
    }
}

internal class ViewPagerAdapterOb(supportFragmentManager: FragmentManager?) :
    FragmentPagerAdapter(supportFragmentManager!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val mList: MutableList<Fragment> = ArrayList()
    override fun getItem(i: Int): Fragment {
        return mList[i]
    }

    override fun getCount(): Int {
        return mList.size
    }

    fun addFragment(fragment: Fragment) {
        mList.add(fragment)
    }
}