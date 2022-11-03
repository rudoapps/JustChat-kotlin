package es.rudo.firebasechat.main.instance

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import es.rudo.firebasechat.data.model.configuration.BackConfiguration
import es.rudo.firebasechat.data.model.configuration.BasicConfiguration
import es.rudo.firebasechat.data.model.configuration.FirebaseConfiguration
import es.rudo.firebasechat.data.model.configuration.MixConfiguration
import es.rudo.firebasechat.ui.chat.ChatActivity
import javax.inject.Inject

class RudoChatInstance @Inject constructor(
    @ApplicationContext val context: Context,
    basicConf: BasicConfiguration
) {

    companion object {
        private lateinit var firebaseAuth: FirebaseAuth
        private lateinit var onTapClient: SignInClient
        private lateinit var basicConf: BasicConfiguration
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
            return if (this::basicConf.isInitialized) {
                basicConf.type
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
    }

    init {
        parseConfiguration(basicConf)
    }

    fun loadChat(client: SignInClient, auth: FirebaseAuth) {
        onTapClient = client
        firebaseAuth = auth
        context.startActivity(Intent(context, ChatActivity::class.java))
    }

    private fun parseConfiguration(conf: BasicConfiguration) {
        basicConf = conf
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
