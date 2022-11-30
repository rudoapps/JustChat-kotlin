package es.rudo.justchat.models

/**
 * This class needs to be implemented in your object to send a basic message
 */
class ChatMessageItem : ChatBaseItem() {
    var text: String? = null
    var userId: String? = null
    var timestamp: Long? = null

    @Transient
    var position: MessagePosition? = null

    enum class MessagePosition {
        SINGLE, BOTTOM, MIDDLE, TOP
    }
}
