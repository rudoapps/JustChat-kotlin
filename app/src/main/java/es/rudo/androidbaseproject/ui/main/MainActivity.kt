package es.rudo.androidbaseproject.ui.main

import android.util.Log
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.identity.* // ktlint-disable no-wildcard-imports
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import es.rudo.androidbaseproject.BuildConfig
import es.rudo.androidbaseproject.R
import es.rudo.androidbaseproject.databinding.ActivityMainBinding
import es.rudo.androidbaseproject.ui.base.BaseActivity
import es.rudo.firebasechat.main.instance.RudoChatInstance
import es.rudo.firebasechat.data.model.configuration.FirebaseConfiguration

@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override val layoutId = R.layout.activity_main

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var signUpRequest: BeginSignInRequest
    private lateinit var rudoChatInstance: RudoChatInstance

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                try {
                    val credentials = oneTapClient.getSignInCredentialFromIntent(result.data)
                    loginWithFirebase(credentials)
                } catch (ex: ApiException) {
                    Log.e("_TAG_", ex.localizedMessage)
                    when (ex.statusCode) {
                        CommonStatusCodes.CANCELED -> {
                            Toast.makeText(this, "One-tap dialog closed", Toast.LENGTH_SHORT).show()
                        }

                        CommonStatusCodes.NETWORK_ERROR -> {
                            Toast.makeText(this, "Check internet connection", Toast.LENGTH_SHORT)
                                .show()
                        }

                        else -> {
                            Toast.makeText(this, ex.localizedMessage, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }

    override fun setUpViews() {
        initObservers()
        initRequests()
        rudoChatInstance =
            RudoChatInstance(
                this,
                FirebaseConfiguration()
            )
        oneTapClient = Identity.getSignInClient(this)
        initListeners()
    }

    private fun initListeners() {
        binding.buttonOpenChat.setOnClickListener {
            viewModel.oneTapSignInWithGoogle(oneTapClient, signInRequest, signUpRequest)
        }
    }

    private fun initRequests() {
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(BuildConfig.CLIENT_ID)
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()

        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(BuildConfig.CLIENT_ID)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            ).build()
    }

    private fun initObservers() {
        viewModel.result.observe(this) {
            launcher.launch(
                IntentSenderRequest.Builder(it.pendingIntent.intentSender).build()
            )
        }

        viewModel.error.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun loginWithFirebase(credentials: SignInCredential) {
        val credential = GoogleAuthProvider.getCredential(credentials.googleIdToken, null)

        FirebaseAuth.getInstance().let { firebaseAuth ->
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    Toast.makeText(this, "Login correct", Toast.LENGTH_SHORT).show()
                    rudoChatInstance.loadChat(oneTapClient, firebaseAuth)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                }
                .addOnCanceledListener {
                    Toast.makeText(this, "Login canceled", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
