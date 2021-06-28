package com.benrostudios.enchante

import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tokenObtain()
    }

    private fun tokenObtain() {
        val mUser = FirebaseAuth.getInstance().currentUser
        d("token", "This is uid ${mUser?.uid}")
        mUser!!.getIdToken(true)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val idToken: String? = task.result?.token
                    d("token", "here is the token $idToken")
                } else {
                    d("token", "some error occured")
                }
            }

    }
}