package es.rudo.firebasechat.domain.models

/**
 * This class needs to be implemented in your object to send a basic message
 */
@Suppress("EqualsOrHashCode")
class Message : ChatItem() {
    var text: String? = null
    var userId: String? = null

    var status: String? = null
    var position: String? = null

    //TODO pendiente de revisar
    override fun equals(other: Any?): Boolean {
        return other is Message && other.id == this.id
    }
}
