package com.benrostudios.enchante.ui.nearby

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.benrostudios.enchante.R
import com.benrostudios.enchante.SplashActivity
import com.benrostudios.enchante.databinding.ActivityMainBinding
import com.benrostudios.enchante.databinding.ActivityNearbyPaymentBinding
import com.benrostudios.enchante.ui.TestActivity
import com.benrostudios.enchante.ui.auth.AuthActivity
import com.benrostudios.enchante.ui.home.HomeActivity
import com.benrostudios.enchante.ui.onboarding.OnboardingActivity
import com.benrostudios.enchante.utils.SharedPrefManager
import com.benrostudios.enchante.utils.show
import com.bumptech.glide.Glide
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.android.inject

class NearbyPaymentActivity : AppCompatActivity() {

    //Nearby
    private lateinit var messageListener: MessageListener
    private lateinit var messageClient: MessagesClient

    private lateinit var binding: ActivityNearbyPaymentBinding
    private val sharedPrefManager: SharedPrefManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNearbyPaymentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        Glide.with(this).load(Firebase.auth.currentUser?.photoUrl).circleCrop()
            .into(binding.nearbyUserImage)
        messageListener = object : MessageListener() {
            override fun onFound(p0: Message) {
                Log.d(TAG, "Found message: ${String(p0.content)}");
            }

            override fun onLost(p0: Message) {
                Log.d(TAG, "Lost message: ${String(p0.content)}");
            }

            override fun onBleSignalChanged(message: Message, bleSignal: BleSignal) {
                Log.i(
                    TAG,
                    "Message: $message has new BLE signal information: $bleSignal"
                )
            }


            override fun onDistanceChanged(p0: Message, p1: Distance) {
                Log.d(TAG, "Found Distance: ${p0.content}");
            }
        }
        Nearby.getMessagesClient(
            this
        ).publish(Message(sharedPrefManager.userPhoneNumber.toByteArray()))
            .addOnSuccessListener {
                Log.d(TAG, "Publishing Nearby")
            }
            .addOnFailureListener {
                Log.d(TAG, it.toString())
            };
        Nearby.getMessagesClient(this).subscribe(messageListener);

        Handler(Looper.getMainLooper()).postDelayed(
            {
                binding.senderImage.show()
                binding.senderName.show()
                binding.senderImage.setOnClickListener {
                    startActivity(Intent(this, NearbyFragmentHolder::class.java))
                    this.finish()
                }
            }, 5000
        )
    }

    override fun onStart() {
        super.onStart()

    }

    companion object {
        private const val TAG = "nearby_activity"
    }

}