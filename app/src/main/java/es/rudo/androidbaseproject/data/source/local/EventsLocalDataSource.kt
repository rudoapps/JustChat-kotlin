package es.rudo.androidbaseproject.data.source.local

import es.rudo.firebasechat.models.Chat
import es.rudo.firebasechat.models.ChatMessageItem
import es.rudo.firebasechat.models.Group
import kotlinx.coroutines.flow.Flow

interface EventsLocalDataSource {
    fun getChats(userId: String): Flow<MutableList<Chat>>
    fun getChatMessages(
        userId: String,
        chatId: String,
        page: Int
    ): Flow<MutableList<ChatMessageItem>>

    fun getGroups(userId: String): Flow<MutableList<Group>>
}
