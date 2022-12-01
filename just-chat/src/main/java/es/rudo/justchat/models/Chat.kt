package es.rudo.justchat.models

import android.util.Log
import java.io.Serializable

class Chat : Serializable, Comparable<Chat> {
    var id: String? = null
    var name: String? = null
    var otherUserId: String? = null
    var otherUserImage: String? = null
    var lastMessage: ChatMessageItem? = null
    var userDeviceToken: String? = null
    var messages: MutableList<ChatMessageItem>? = null

    fun getTimestamp(): Long {
        return lastMessage?.timestamp ?: System.currentTimeMillis()
    }

    override fun compareTo(other: Chat): Int {
//        val currentTimestamp = getTimestamp()
//        val otherTimestamp = other.getTimestamp()
//        return if (currentTimestamp > otherTimestamp) {
//            -1
//        } else if (currentTimestamp < otherTimestamp) {
//            1
//        } else {
//            0
//        }
        lastMessage?.timestamp?.let { currentTimestamp ->
            other.lastMessage?.timestamp?.let { otherTimestamp ->
                return if (currentTimestamp > otherTimestamp) {
                    Log.d("_TAG_", "")
                    -1
                } else if (currentTimestamp < otherTimestamp) {
                    1
                } else {
                    1
                }
            }
        }
        return 1
    }
}
