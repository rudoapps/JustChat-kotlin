package es.rudo.justchat.models

import android.util.Log
import es.rudo.justchat.helpers.extensions.getAllDate
import java.io.Serializable

class Chat : Serializable, Comparable<Chat> {
    var id: String? = null
    var name: String? = null
    var otherUserId: String? = null
    var otherUserImage: String? = null
    var lastMessage: ChatMessageItem? = null
    var userDeviceToken: String? = null
    var messages: MutableList<ChatMessageItem>? = null

    private fun getCorrectTimestamp(): Long {
        return lastMessage?.timestamp ?: 315529200
    }

    override fun compareTo(other: Chat): Int {
        val currentTimestamp = getCorrectTimestamp()
        val otherTimestamp = other.getCorrectTimestamp()
        return if (currentTimestamp > otherTimestamp) {
            -1
        } else if (currentTimestamp < otherTimestamp) {
            1
        } else {
            0
        }
    }
}
