package es.rudo.androidbaseproject.ui.main

import android.util.Log
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.identity.* // ktlint-disable no-wildcard-imports
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import es.rudo.androidbaseproject.BuildConfig
import es.rudo.androidbaseproject.R
import es.rudo.androidbaseproject.databinding.ActivityMainBinding
import es.rudo.androidbaseproject.helpers.extensions.saveUserId
import es.rudo.androidbaseproject.helpers.setClickWithDebounce
import es.rudo.androidbaseproject.ui.base.BaseActivity
import es.rudo.firebasechat.helpers.extensions.isNetworkAvailable
import es.rudo.firebasechat.interfaces.Events
import es.rudo.firebasechat.main.instance.JustChat

@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override val layoutId = R.layout.activity_main

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var signUpRequest: BeginSignInRequest
    private lateinit var justChat: JustChat
    private lateinit var events: Events

    companion object {
        private lateinit var firebaseAuth: FirebaseAuth
        private lateinit var onTapClient: SignInClient

        fun getFirebaseAuth(): FirebaseAuth? {
            return if (this::firebaseAuth.isInitialized) {
                firebaseAuth
            } else {
                null
            }
        }

        fun getOnTapClient(): SignInClient? {
            return if (this::onTapClient.isInitialized) {
                onTapClient
            } else {
                null
            }
        }
    }

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
        initListeners()
        initObservers()
        initRequests()
        events = viewModel.events
        oneTapClient = Identity.getSignInClient(this)
    }

    override fun onResume() {
        super.onResume()
        if (getOnTapClient() != null && getFirebaseAuth() != null) {
            logout()
        }
    }

    private fun initListeners() {
        binding.buttonOpenChat.setClickWithDebounce {
            viewModel.oneTapSignInWithGoogle(oneTapClient, signInRequest, signUpRequest)
        }
    }

    private fun initRequests() {
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(BuildConfig.CLIENT_ID)
                    .setFilterByAuthorizedAccounts(false)
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

    private fun logout() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.CLIENT_ID)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.signOut()
            .addOnCompleteListener {
                Toast.makeText(
                    this,
                    getString(es.rudo.firebasechat.R.string.correct),
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    getString(es.rudo.firebasechat.R.string.error_closing_session),
                    Toast.LENGTH_SHORT
                ).show()
            }
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

        viewModel.userInitialized.observe(this) {
            if (it.success == false) {
                Toast.makeText(this, it.error?.message.toString(), Toast.LENGTH_SHORT).show()
            } else {
                if (it.exists == true) {
                    justChat.openChatLists()
//                    justChat.openChat("1669114264338-AfRJVCD8iadBjEbYso7ql4KmzpexnG")
                } else {
                    viewModel.initCurrentUserChats(isNetworkAvailable)
                }
            }
        }

        viewModel.listChatId.observe(this) {
            if (it.isNotEmpty()) {
                viewModel.initOtherUsersChats(isNetworkAvailable, it)
            }
        }

        viewModel.chatsInitialized.observe(this) {
            if (it.success == false) {
                Toast.makeText(this, it.error?.message.toString(), Toast.LENGTH_SHORT).show()
            } else {
                justChat.openChatLists()
//                justChat.openChat("1669114264338-AfRJVCD8iadBjEbYso7ql4KmzpexnG")
            }
        }
    }

    private fun loginWithFirebase(credentials: SignInCredential) {
        val credential = GoogleAuthProvider.getCredential(credentials.googleIdToken, null)
        FirebaseAuth.getInstance().let { firebaseAuth ->
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    Toast.makeText(this, "Login correct", Toast.LENGTH_SHORT).show()
                    justChat = JustChat(this, firebaseAuth.currentUser?.uid, events)
                    MainActivity.firebaseAuth = firebaseAuth
                    onTapClient = oneTapClient
                    saveUserId(firebaseAuth.currentUser?.uid)
                    initUser()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                }
                .addOnCanceledListener {
                    Toast.makeText(this, "Login canceled", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun initUser() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (!it.isSuccessful) {
                return@addOnCompleteListener
            }
            viewModel.initUser(isNetworkAvailable, it.result)
        }
    }
}
