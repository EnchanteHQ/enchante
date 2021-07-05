package com.benrostudios.enchante.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.benrostudios.enchante.R
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.*


class TestActivity : AppCompatActivity() {

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

        val poptions = PublishOptions.Builder().setStrategy(Strategy.DEFAULT).build()
        Nearby.getMessagesClient(
            this, MessagesOptions.Builder()
                .setPermissions(NearbyPermissions.BLE)
                .build()
        ).publish(Message("Suppppp".toByteArray()), poptions).addOnFailureListener {
            Log.d(TAG, it.toString())
        };
        Nearby.getMessagesClient(this).subscribe(messageListener, options);

        Nearby.getMessagesClient(
            this, MessagesOptions.Builder()
                .setPermissions(NearbyPermissions.BLE)
                .build()
        ).publish(Message("lmfao".toByteArray())).addOnFailureListener {
            Log.d(TAG, it.toString())
        };
//        Nearby.getMessagesClient(this).publish(Message("Suppppp".toByteArray()))
//        Nearby.getMessagesClient(this).subscribe(messageListener)

        Handler(Looper.getMainLooper()).postDelayed(
            {

                Nearby.getMessagesClient(
                    this, MessagesOptions.Builder()
                        .setPermissions(NearbyPermissions.BLE)
                        .build()
                ).publish(Message("lmfa2o".toByteArray())).addOnFailureListener {
                    Log.d(TAG, it.toString())
                };


            }, 5000
        )
    }

    override fun onStart() {
//        Nearby.getMessagesClient(this).publish(Message("Suppppp".toByteArray()))
//        Nearby.getMessagesClient(this).subscribe(messageListener)
//        try {
//            messageClient.publish(Message("Suppppp".toByteArray()));
//            messageClient.subscribe(messageListener);
//        } catch(e: Exception){
//            Log.d(TAG, e.toString())
//        }
        super.onStart()
    }

    override fun onStop() {
        messageClient.unsubscribe(messageListener);
        super.onStop()
    }

    companion object {
        private const val TAG = "test_activity"
    }
}