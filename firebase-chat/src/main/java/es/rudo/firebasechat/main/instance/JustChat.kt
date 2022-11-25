package es.rudo.firebasechat.main.instance

import android.content.Context
import android.content.Intent
import es.rudo.firebasechat.helpers.extensions.saveUserId
import es.rudo.firebasechat.interfaces.Events
import es.rudo.firebasechat.ui.chat_list.ChatListActivity

class JustChat constructor(val context: Context, val userId: String?, events: Events) {

    companion object {
        var userId: String? = null
        var events: Events? = null
    }

    init {
        JustChat.userId = userId
        JustChat.events = events
        context.saveUserId(userId)
    }

    fun loadChat() {
        context.startActivity(Intent(context, ChatListActivity::class.java))
    }
}
