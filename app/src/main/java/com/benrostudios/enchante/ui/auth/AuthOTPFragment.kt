package com.benrostudios.enchante.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.benrostudios.enchante.R
import com.benrostudios.enchante.databinding.FragmentAuthOTPBinding
import com.benrostudios.enchante.databinding.FragmentNearbyTransactionDetailsBinding
import com.benrostudios.enchante.ui.home.HomeActivity
import com.benrostudios.enchante.utils.SharedPrefManager
import com.benrostudios.enchante.utils.hide
import com.benrostudios.enchante.utils.show
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit


class AuthOTPFragment : Fragment() {

    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private var _binding: FragmentAuthOTPBinding? = null
    private val binding get() = _binding!!
    private val sharedPrefManager: SharedPrefManager by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthOTPBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.sendOtpButton.setOnClickListener {
            verifyPhone(binding.numberInputField.text.toString())
            binding.sendOtpPb.show()
            binding.sendOtpButton.hide()
            sharedPrefManager.userPhoneNumber = binding.numberInputField.text.toString()
        }

        binding.otpView.setOtpCompletionListener {
            verifyCode(it)
        }

        binding.verifyButton.setOnClickListener {
            verifyCode(binding.otpView.text.toString())
        }
    }

    private fun verifyPhone(number: String) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {

                Log.w(TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d(TAG, "onCodeSent:$verificationId")
                binding.sendOtpButton.hide()
                binding.otpView.show()
                binding.verifyButton.show()
                binding.sendOtpPb.hide()
                storedVerificationId = verificationId
                resendToken = token
            }
        }

        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        binding.sendOtpButton.hide()
        binding.verifyButton.hide()
        binding.authVerifyPb.show()
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    this.findNavController().navigate(R.id.action_authOTPFragment_to_googleAuth)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        binding.verifyButton.show()
                        binding.authVerifyPb.hide()
                    }
                }
            }
    }

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
        signInWithPhoneAuthCredential(credential)
    }

    companion object {
        const val TAG = "authOTP"
    }


}