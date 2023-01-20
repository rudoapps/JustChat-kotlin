package es.rudo.justchat.main.instance

import android.content.Context
import android.content.Intent
import es.rudo.justchat.helpers.Constants
import es.rudo.justchat.helpers.preferences.AppPreferences
import es.rudo.justchat.interfaces.Events
import es.rudo.justchat.models.Chat
import es.rudo.justchat.models.ChatStyle
import es.rudo.justchat.models.error.ChatNotFound
import es.rudo.justchat.ui.chat.ChatActivity
import es.rudo.justchat.ui.chat_list.ChatListActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class JustChat(val context: Context?, val userId: String?, val style: ChatStyle?, events: Events?) {

    private constructor(builder: Builder) : this(
        builder.context,
        builder.userId,
        builder.style,
        builder.events
    )

    class Builder {
        var context: Context? = null
            private set
        var userId: String? = null
            private set
        var style: ChatStyle? = null
            private set
        var events: Events? = null
            private set

        fun provideContext(context: Context) = apply { this.context = context }
        fun setUserId(userId: String?) = apply { this.userId = userId }
        fun setChatStyle(style: ChatStyle?) = apply { this.style = style }
        fun setEventsImplementation(events: Events) = apply { this.events = events }

        fun build() = JustChat(this)
    }

    init {
        JustChat.userId = userId
        JustChat.style = style
        JustChat.events = events
        appPreferences = AppPreferences(
            context?.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)
        )
        appPreferences?.userId = userId
    }

    fun openChatLists() {
        context?.startActivity(Intent(context, ChatListActivity::class.java))
    }

    suspend fun openChat(chatId: String) {
        events?.getChat(userId.toString(), chatId)?.collect { chat ->
            withContext(Dispatchers.Main) {
                if (chatNoExist(chat)) {
                    throw ChatNotFound(
                        "This chat '$chatId' does not exist in the current user '$userId'.",
                        RuntimeException(
                            "FATAL EXCEPTION: This chat '$chatId' does not exist in the current user '$userId'."
                        )
                    )
                } else {
                    val intent = Intent(context, ChatActivity::class.java)
                    intent.putExtra(Constants.CHAT, chat)
                    context?.startActivity(intent)
                }
            }
        }
    }

    private fun chatNoExist(chat: Chat): Boolean {
        return (chat.name == null && chat.otherUserId == null && chat.otherUserImage == null && chat.userDeviceToken == null)
    }

    companion object {
        var userId: String? = null
        var style: ChatStyle? = null
        var events: Events? = null
        var appPreferences: AppPreferences? = null
    }
}
