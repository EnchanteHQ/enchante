package com.benrostudios.enchante.ui.nearby

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.benrostudios.enchante.R
import com.benrostudios.enchante.databinding.ActivityMainBinding
import com.benrostudios.enchante.databinding.ActivityNearbyPaymentBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class NearbyPaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNearbyPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNearbyPaymentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        Glide.with(this).load(Firebase.auth.currentUser?.photoUrl).circleCrop()
            .into(binding.nearbyPaymentUserImg)
        Glide.with(this).load(Firebase.auth.currentUser?.photoUrl).circleCrop()
            .into(binding.nearbyPaymentTosendImg4)
    }
}