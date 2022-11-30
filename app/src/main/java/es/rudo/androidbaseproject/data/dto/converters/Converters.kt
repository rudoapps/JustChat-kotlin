package es.rudo.androidbaseproject.data.dto.converters

import es.rudo.androidbaseproject.data.dto.MessageBack
import es.rudo.firebasechat.models.ChatMessageItem

fun ChatMessageItem.toMessageBack(): MessageBack {
    return MessageBack().apply {
        text = this@toMessageBack.text
        userId = this@toMessageBack.userId
    }
}
