package com.benrostudios.enchante

import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.benrostudios.enchante.ui.auth.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    private val authViewModel: AuthViewModel by viewModel()
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
                    d(TAG, "here is the token $idToken")
                    if (idToken != null) {
                        callJwtToken(idToken)
                    }
                } else {
                    d(TAG, "some error occured")
                }
            }

    }

    private fun callJwtToken(token: String) {
        authViewModel.login(token)
        authViewModel.response.observe(this, Observer {
            if (it.data.jwtToken != null) {
                d(TAG, "${it.data.jwtToken}")
            }
        })
    }

    companion object {
        const val TAG = "main_activity"
    }

}