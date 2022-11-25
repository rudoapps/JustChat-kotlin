package es.rudo.androidbaseproject.ui.main

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import dagger.hilt.android.lifecycle.HiltViewModel
import es.rudo.androidbaseproject.data.source.preferences.AppPreferences
import es.rudo.androidbaseproject.domain.EventsUseCase
import es.rudo.androidbaseproject.ui.base.BaseViewModel
import es.rudo.firebasechat.interfaces.Events
import es.rudo.firebasechat.models.results.ResultInfo
import es.rudo.firebasechat.models.results.ResultUserChat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val preferences: AppPreferences,
    private val eventsUseCase: EventsUseCase
) : BaseViewModel() {

    val result = MutableLiveData<BeginSignInResult>()
    val error = MutableLiveData<String>()
    val userInitialized = MutableLiveData<ResultUserChat>()
    val listChatId = MutableLiveData<MutableList<Pair<String, String>>>()
    val chatsInitialized = MutableLiveData<ResultInfo>()
    lateinit var events: Events

    companion object {
        var eventsUseCase: EventsUseCase? = null
    }

    override fun initData(data: Bundle) {
        MainViewModel.eventsUseCase = eventsUseCase
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

    fun initUser(isNetworkAvailable: Boolean, deviceToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            eventsUseCase.initUser(isNetworkAvailable, deviceToken).collect {
                withContext(Dispatchers.Main) {
                    userInitialized.value = it
                }
            }
        }
    }

    fun initCurrentUserChats(isNetworkAvailable: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            eventsUseCase.initCurrentUserChats(isNetworkAvailable).collect {
                withContext(Dispatchers.Main) {
                    listChatId.value = it
                }
            }
        }
    }

    fun initOtherUsersChats(
        isNetworkAvailable: Boolean,
        listChatId: MutableList<Pair<String, String>>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            eventsUseCase.initOtherUsersChats(isNetworkAvailable, listChatId).collect {
                withContext(Dispatchers.Main) {
                    chatsInitialized.value = it
                }
            }
        }
    }
}
