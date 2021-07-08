package com.benrostudios.enchante.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.benrostudios.enchante.R
import com.benrostudios.enchante.data.models.requests.RegistrationRequest
import com.benrostudios.enchante.databinding.ActivityAuthBinding
import com.benrostudios.enchante.databinding.FragmentAuthOTPBinding
import com.benrostudios.enchante.databinding.FragmentGoogleAuthBinding
import com.benrostudios.enchante.ui.home.HomeActivity
import com.benrostudios.enchante.ui.onboarding.OnboardingActivity
import com.benrostudios.enchante.ui.viewmodels.ApiViewModel
import com.benrostudios.enchante.ui.viewmodels.AuthViewModel
import com.benrostudios.enchante.utils.SharedPrefManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class GoogleAuth : Fragment() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var previewRequest: ActivityResultLauncher<Intent>
    private val sharedPrefManager: SharedPrefManager by inject()
    private val authViewModel: AuthViewModel by viewModel()
    private val apiViewModel: ApiViewModel by viewModel()

    private var _binding: FragmentGoogleAuthBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerAuthIntent()
        configureGso()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoogleAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.gSign.setOnClickListener {
            signIn()
        }
    }

    private fun registerAuthIntent() {
        previewRequest =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == AppCompatActivity.RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                    try {
                        val account = task.getResult(ApiException::class.java)!!
                        firebaseAuthWithGoogle(account.idToken!!)
                    } catch (e: ApiException) {
                        Log.w(AuthActivity.TAG, "Google sign in failed", e)
                    }
                }
            }
    }

    private fun configureGso() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        auth = Firebase.auth
    }


    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        previewRequest.launch(signInIntent)
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(AuthActivity.TAG, "signInWithCredential:success")
                    tokenObtain()
                } else {
                    Log.w(AuthActivity.TAG, "signInWithCredential:failure", task.exception)
                    //TODO: Handle login failure
                }
            }
    }

    private fun tokenObtain() {
        val mUser = FirebaseAuth.getInstance().currentUser
        Log.d("token", "This is uid ${mUser?.uid}")
        mUser!!.getIdToken(true)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val idToken: String? = task.result?.token
                    Log.d(HomeActivity.TAG, "here is the token $idToken")
                    if (idToken != null) {
                        callJwtToken(idToken)
                    }
                } else {
                    Log.d(HomeActivity.TAG, "some error occured")
                    FirebaseAuth.getInstance().signOut()
                    sharedPrefManager.nukeSharedPrefs()
                    startActivity(Intent(requireContext(), OnboardingActivity::class.java))
                }
            }
    }

    private fun callJwtToken(token: String) {
        authViewModel.login(token)
        authViewModel.response.observe(this, Observer {
            if (it.data.jwtToken != null) {
                Log.d(HomeActivity.TAG, "${it.data.jwtToken}")
                sharedPrefManager.jwtStored = it.data.jwtToken
                registerUser()
            }
        })
    }

    private fun registerUser() {
        val regRequest = RegistrationRequest(
            "",
            Firebase.auth.currentUser?.photoUrl.toString(),
            listOf("Hardworking"),
            sharedPrefManager.userPhoneNumber,
            ""
        )

        apiViewModel.registerRoute(regRequest)
        apiViewModel.regResponse.observe(this, Observer {
            it.let { data ->
                if (data.success == "true") {
                    startActivity(Intent(requireContext(), HomeActivity::class.java))
                }
            }

        })

    }
}