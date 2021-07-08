package com.benrostudios.enchante.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.benrostudios.enchante.R
import com.benrostudios.enchante.utils.SharedPrefManager
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.*
import org.koin.android.ext.android.inject


class TestActivity : AppCompatActivity() {

    private val sharedPrefManager: SharedPrefManager by inject()

    //Nearby
    private lateinit var messageListener: MessageListener
    private lateinit var messageClient: MessagesClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        messageListener = object : MessageListener() {
            override fun onFound(p0: Message) {
                Log.d(TAG, "Found message: ${String(p0.content)}");
            }

            override fun onLost(p0: Message) {
                Log.d(TAG, "Lost message: ${String(p0.content)}");
            }

            override fun onBleSignalChanged(message: Message, bleSignal: BleSignal) {
                Log.i(TAG, "Message: $message has new BLE signal information: $bleSignal")
            }


            override fun onDistanceChanged(p0: Message, p1: Distance) {
                Log.d(TAG, "Found Distance: ${p0.content}");
            }
        }
        messageClient = Nearby.getMessagesClient(
            this, MessagesOptions.Builder()
                .setPermissions(NearbyPermissions.BLE)
                .build()
        )
        val options = SubscribeOptions.Builder()
            .setStrategy(Strategy.BLE_ONLY)
            .build()

        val pOptions = PublishOptions.Builder().setStrategy(Strategy.DEFAULT).build()
        Nearby.getMessagesClient(
            this, MessagesOptions.Builder()
                .setPermissions(NearbyPermissions.BLE)
                .build()
        ).publish(Message(sharedPrefManager.userPhoneNumber.toByteArray()), pOptions)
            .addOnFailureListener {
                Log.d(TAG, it.toString())
            };
        Nearby.getMessagesClient(this).subscribe(messageListener, options);

    }

    override fun onStop() {
        messageClient.unsubscribe(messageListener);
        super.onStop()
    }

    companion object {
        private const val TAG = "test_activity"
    }
}