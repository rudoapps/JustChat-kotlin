package es.rudo.firebasechat.models

import es.rudo.firebasechat.models.ChatBaseItem

/**
 * This class needs to be implemented in your object to send a basic message
 */
class ChatMessageItem : ChatBaseItem() {
    var text: String? = null
    var userId: String? = null
    var timestamp: Long? = null

    var status: String? = null
    var position: String? = null
}
