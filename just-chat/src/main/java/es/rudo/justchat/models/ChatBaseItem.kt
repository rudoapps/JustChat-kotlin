package es.rudo.justchat.models

import java.io.Serializable

@Suppress("EqualsOrHashCode")
open class ChatBaseItem : Serializable {
    var id: String? = null

    // TODO pendiente de revisar
    override fun equals(other: Any?): Boolean {
        return other is ChatMessageItem && other.id == this.id
    }
}
