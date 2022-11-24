package es.rudo.firebasechat.data.dto.converters

import es.rudo.firebasechat.data.dto.MessageBack
import es.rudo.firebasechat.domain.models.ChatMessageItem

fun ChatMessageItem.toMessageBack(): MessageBack {
    return MessageBack().apply {
        text = this@toMessageBack.text
        userId = this@toMessageBack.userId
    }
}
