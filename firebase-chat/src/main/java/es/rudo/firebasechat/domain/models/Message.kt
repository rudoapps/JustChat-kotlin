package es.rudo.firebasechat.domain.models

import java.io.Serializable

/**
 * This class need to be implemented in your object to send a basic message
 */
class Message : Serializable {
    var id: String? = null
    var text: String? = null
    var timestamp: Long? = null
    var userId: String? = null
}
