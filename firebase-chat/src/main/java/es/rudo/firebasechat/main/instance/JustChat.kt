package es.rudo.firebasechat.main.instance

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import es.rudo.firebasechat.domain.models.configuration.BackConfiguration
import es.rudo.firebasechat.domain.models.configuration.BasicConfiguration
import es.rudo.firebasechat.domain.models.configuration.FirebaseConfiguration
import es.rudo.firebasechat.domain.models.configuration.MixConfiguration
import es.rudo.firebasechat.helpers.Constants
import es.rudo.firebasechat.ui.chat_list.ChatListActivity

class JustChat constructor(
    val context: Context,
    basicConf: BasicConfiguration
) {
    companion object {
        private lateinit var firebaseAuth: FirebaseAuth
        private lateinit var onTapClient: SignInClient
        private lateinit var basicConfiguration: BasicConfiguration
        private lateinit var firebaseConfiguration: FirebaseConfiguration
        private lateinit var backConfiguration: BackConfiguration
        private lateinit var mixConfiguration: MixConfiguration

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

        fun getType(): BasicConfiguration.Type? {
            return if (this::basicConfiguration.isInitialized) {
                basicConfiguration.type
            } else {
                null
            }
        }

        fun getNodeFirebase(): String? {
            return if (this::firebaseConfiguration.isInitialized) {
                firebaseConfiguration.nodeFirebase
            } else {
                null
            }
        }

        fun getProjectName(): String? {
            return if (this::firebaseConfiguration.isInitialized) {
                firebaseConfiguration.firebaseProjectName
            } else {
                null
            }
        }
    }

    init {
        parseConfiguration(basicConf)
    }

    fun loadChat(client: SignInClient, auth: FirebaseAuth) {
        onTapClient = client
        firebaseAuth = auth
        val preferences = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)
        preferences.edit()?.putString(Constants.USER_ID_PREFERENCES, auth.currentUser?.uid)?.apply()
        context.startActivity(Intent(context, ChatListActivity::class.java))
    }

    private fun parseConfiguration(conf: BasicConfiguration) {
        basicConfiguration = conf
        when (conf) {
            is FirebaseConfiguration -> {
                firebaseConfiguration = conf
            }
            is BackConfiguration -> {
                backConfiguration = conf
            }
            is MixConfiguration -> {
                mixConfiguration = conf
            }
        }
    }
}
