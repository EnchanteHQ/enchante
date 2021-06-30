package com.benrostudios.enchante.ui.home

import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.benrostudios.enchante.R
import com.benrostudios.enchante.databinding.ActivityAuthBinding
import com.benrostudios.enchante.databinding.ActivityMainBinding
import com.benrostudios.enchante.ui.auth.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private val authViewModel: AuthViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        tokenObtain()
        navController = findNavController(R.id.nav_host_fragment_home_activity)
        binding.bottomNavigationViewHomeActivity.setupWithNavController(navController)
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