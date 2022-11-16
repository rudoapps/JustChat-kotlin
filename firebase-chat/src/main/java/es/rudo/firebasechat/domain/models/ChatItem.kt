package es.rudo.firebasechat.domain.models

import java.io.Serializable

open class ChatItem : Serializable {
    var id: String? = null
    var timestamp: Long? = null
}