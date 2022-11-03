package es.rudo.androidbaseproject.ui.main

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import dagger.hilt.android.lifecycle.HiltViewModel
import es.rudo.androidbaseproject.data.source.local.preferences.AppPreferences
import es.rudo.androidbaseproject.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val preferences: AppPreferences
) : BaseViewModel() {

    val result = MutableLiveData<BeginSignInResult>()
    val error = MutableLiveData<String>()

    override fun initData(data: Bundle) {
    }

    fun oneTapSignInWithGoogle(
        oneTapClient: SignInClient,
        signInRequest: BeginSignInRequest,
        signUpRequest: BeginSignInRequest
    ) {
        try {
            oneTapClient.beginSignIn(signInRequest).addOnCompleteListener {
                try {
                    result.value = it.result
                } catch (ex: Exception) {
                    oneTapSignUpWithGoogle(oneTapClient, signUpRequest)
                    Log.e("_TAG_", ex.localizedMessage)
                    error.value = ex.localizedMessage
                }
            }.addOnFailureListener {
                try {
                    oneTapSignUpWithGoogle(oneTapClient, signUpRequest)
                } catch (ex: Exception) {
                    Log.e("_TAG_", it.localizedMessage)
                    error.value = ex.localizedMessage
                }
            }
        } catch (ex: Exception) {
            Log.e("_TAG_", ex.localizedMessage.toString())
            error.value = ex.localizedMessage
        }
    }

    private fun oneTapSignUpWithGoogle(
        oneTapClient: SignInClient,
        signUpRequest: BeginSignInRequest
    ) {
        try {
            oneTapClient.beginSignIn(signUpRequest).addOnCompleteListener {
                try {
                    result.value = it.result
                } catch (ex: Exception) {
                    Log.e("_TAG_", ex.localizedMessage)
                    error.value = ex.localizedMessage
                }
            }
        } catch (ex: Exception) {
            Log.e("_TAG_", ex.localizedMessage)
            error.value = ex.localizedMessage
        }
    }
}
