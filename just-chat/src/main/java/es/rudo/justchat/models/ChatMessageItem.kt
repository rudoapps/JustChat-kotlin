package es.rudo.justchat.models

/**
 * This class needs to be implemented in your object to send a basic message
 */
@Suppress("EqualsOrHashCode")
class ChatMessageItem : ChatBaseItem() {
    var text: String? = null
    var userId: String? = null
    var timestamp: Long? = null

    @Transient
    var position: MessagePosition? = null

    override fun equals(other: Any?): Boolean {
        return other is ChatMessageItem && other.id == this.id && other.position == this.position
    }

    enum class MessagePosition {
        SINGLE, BOTTOM, MIDDLE, TOP
    }
}
