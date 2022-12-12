package es.rudo.androidbaseproject.data.dto.converters

import es.rudo.androidbaseproject.data.dto.MessageBack
import es.rudo.justchat.models.ChatMessageItem

fun ChatMessageItem.toMessageBack(): MessageBack {
    return MessageBack().apply {
        text = this@toMessageBack.text
        userId = this@toMessageBack.userId
    }
}
