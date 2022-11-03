package es.rudo.firebasechat.data.model.chats

/**
 * This class need to be implemented in your object to send a basic message
 */
class Message : Comparable<Message> {
    var id: String? = null
    var text: String? = null
    var timestamp: Long? = null
    var userId: String? = null

    override fun compareTo(other: Message): Int {
        timestamp?.let { currentTimestamp ->
            other.timestamp?.let { otherTimestamp ->
                return currentTimestamp.compareTo(otherTimestamp)
            }
        }
        return 0
    }
}
