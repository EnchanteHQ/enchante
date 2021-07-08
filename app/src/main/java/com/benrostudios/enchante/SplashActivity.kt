package com.benrostudios.enchante

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.benrostudios.enchante.ui.auth.AuthActivity
import com.benrostudios.enchante.ui.home.HomeActivity
import com.benrostudios.enchante.ui.onboarding.OnboardingActivity
import com.benrostudios.enchante.utils.SharedPrefManager
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity() {

    private val sharedPrefManager: SharedPrefManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        Handler(Looper.getMainLooper()).postDelayed(
            {
                if (sharedPrefManager.firstOpen) {
                    startActivity(Intent(this, OnboardingActivity::class.java))
                    this.finish()
                } else {
                    if (FirebaseAuth.getInstance().currentUser != null) {
                        startActivity(Intent(this, HomeActivity::class.java))
                        this.finish()
                    } else {
                        startActivity(Intent(this, AuthActivity::class.java))
                        this.finish()
                    }
                }

            }, SPLASH_TIME_OUT
        )


    }

    companion object {
        private const val SPLASH_TIME_OUT = 500L

    }
}