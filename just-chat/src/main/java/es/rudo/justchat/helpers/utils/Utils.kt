package es.rudo.justchat.helpers.utils

import es.rudo.justchat.main.instance.JustChat

val userId: String?
    get() =
        JustChat.userId ?: run {
            JustChat.appPreferences?.userId
        }
