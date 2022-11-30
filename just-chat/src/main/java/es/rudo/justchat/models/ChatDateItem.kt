package es.rudo.justchat.models

@Suppress("EqualsOrHashCode")
class ChatDateItem : ChatBaseItem() {
    var originalTimestamp: Long? = null
    var date: String? = null

    override fun equals(other: Any?): Boolean {
        return other is ChatDateItem && other.id == this.id && other.date == this.date
    }
}
