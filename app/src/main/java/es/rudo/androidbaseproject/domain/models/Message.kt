package es.rudo.androidbaseproject.domain.models

import java.io.Serializable

/**
 * This class needs to be implemented in your object to send a basic message
 */
class Message : Serializable {
    var id: String? = null
    var text: String? = null
    var timestamp: Long? = null
    var userId: String? = null

    var status: String? = null
    var position: String? = null

    //TODO pendiente de revisar
    override fun equals(other: Any?): Boolean {
        return other is Message && other.id == this.id
    }
}
