package com.benrostudios.enchante.ui.nearby

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.benrostudios.enchante.R
import com.benrostudios.enchante.databinding.ActivityNearbyFragmentHolderBinding
import com.benrostudios.enchante.databinding.ActivityNearbyPaymentBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class NearbyFragmentHolder : AppCompatActivity() {
    private lateinit var binding: ActivityNearbyFragmentHolderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNearbyFragmentHolderBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        Glide.with(this).load(Firebase.auth.currentUser?.photoUrl)
            .into(binding.nearbyPaymentUserImg)
        Glide.with(this).load(R.drawable.ic_avatar).into(binding.nearbyPaymentTosendImg4)
        binding.nearbyPaymentDescription.text = this.getString(R.string.pay_to)
    }
}