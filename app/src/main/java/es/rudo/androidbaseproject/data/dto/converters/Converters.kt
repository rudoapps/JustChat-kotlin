package es.rudo.androidbaseproject.data.dto.converters

import es.rudo.androidbaseproject.data.dto.MessageBack
import es.rudo.firebasechat.domain.models.Message

fun Message.toMessageBack(): MessageBack {
    return MessageBack().apply {
        text = this@toMessageBack.text
        userId = this@toMessageBack.userId
    }
}
