package es.rudo.justchat.models

import androidx.annotation.ColorInt
import java.io.Serializable

data class ChatStyle(
    @ColorInt var outgoingMessageColor: Int? = null,
    @ColorInt var incomingMessageColor: Int? = null,
    var outgoingMessagePadding: Int? = null,
    var incomingMessagePadding: Int? = null,
    var showMessageTime: Boolean = true
) : Serializable