package es.rudo.firebasechat.main.instance

import android.content.Context
import android.content.Intent
import es.rudo.firebasechat.helpers.Constants
import es.rudo.firebasechat.helpers.preferences.AppPreferences
import es.rudo.firebasechat.interfaces.Events
import es.rudo.firebasechat.ui.chat_list.ChatListActivity

class JustChat constructor(val context: Context, val userId: String?, events: Events) {

    companion object {
        var userId: String? = null
        var events: Events? = null
        var appPreferences: AppPreferences? = null
    }

    init {
        JustChat.userId = userId
        JustChat.events = events
        appPreferences = AppPreferences(
            context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)
        )
        appPreferences?.userId = userId
    }

    fun openChatLists() {
        context.startActivity(Intent(context, ChatListActivity::class.java))
    }

    fun openChat(chatId: String) {

    }
}
