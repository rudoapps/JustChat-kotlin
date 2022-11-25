package es.rudo.firebasechat.main.instance

import android.content.Context
import android.content.Intent
import es.rudo.firebasechat.helpers.extensions.saveUserId
import es.rudo.firebasechat.ui.chat_list.ChatListActivity

class JustChat constructor(val context: Context, val userId: String?) {

    companion object {
        var userId: String? = null
    }

    init {
        JustChat.userId = userId
        context.saveUserId(userId)
    }

    fun loadChat() {
        context.startActivity(Intent(context, ChatListActivity::class.java))
    }
}
