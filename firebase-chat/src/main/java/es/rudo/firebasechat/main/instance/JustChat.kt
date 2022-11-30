package es.rudo.firebasechat.main.instance

import android.content.Context
import android.content.Intent
import es.rudo.firebasechat.helpers.Constants
import es.rudo.firebasechat.helpers.preferences.AppPreferences
import es.rudo.firebasechat.interfaces.Events
import es.rudo.firebasechat.models.Chat
import es.rudo.firebasechat.ui.chat.ChatActivity
import es.rudo.firebasechat.ui.chat_list.ChatListActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

class JustChat(val context: Context?, val userId: String?, events: Events?) {

    private constructor(builder: Builder) : this(builder.context, builder.userId, builder.events)

    class Builder {
        var context: Context? = null
            private set
        var userId: String? = null
            private set
        var events: Events? = null
            private set

        fun provideContext(context: Context) = apply { this.context = context }
        fun setUserId(userId: String?) = apply { this.userId = userId }
        fun setEventsImplementation(events: Events) = apply { this.events = events }

        fun build() = JustChat(this)
    }

    init {
        JustChat.userId = userId
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
                    throw Exception("FATAL EXCEPTION: This chat does not exist in the current user")
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
        var events: Events? = null
        var appPreferences: AppPreferences? = null
    }
}
