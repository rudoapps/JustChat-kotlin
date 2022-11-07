package es.rudo.firebasechat.data.source.remote.impl

import com.google.firebase.database.*
import es.rudo.firebasechat.data.model.chats.Chat
import es.rudo.firebasechat.data.model.chats.ChatInfo
import es.rudo.firebasechat.data.model.chats.Group
import es.rudo.firebasechat.data.model.chats.Message
import es.rudo.firebasechat.data.model.chats.firebase_chat.EmptyChat
import es.rudo.firebasechat.data.model.configuration.BasicConfiguration
import es.rudo.firebasechat.data.model.result.ResultInfo
import es.rudo.firebasechat.data.model.result.ResultUserChat
import es.rudo.firebasechat.data.source.remote.EventsRemoteDataSource
import es.rudo.firebasechat.helpers.Constants.DEFAULT_USER_PHOTO
import es.rudo.firebasechat.helpers.Constants.LIMIT_MESSAGES
import es.rudo.firebasechat.main.instance.RudoChatInstance
import es.rudo.firebasechat.utils.generateId
import es.rudo.firebasechat.utils.getPair
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import kotlin.collections.ArrayList

class EventsRemoteDataSourceImpl @Inject constructor(
    private val databaseReference: DatabaseReference,
    private val type: BasicConfiguration.Type
) : EventsRemoteDataSource {

    override fun initUser(): Flow<ResultUserChat> {
        return callbackFlow {
            when (type) {
                BasicConfiguration.Type.FIREBASE -> {
                    databaseReference.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(users: DataSnapshot) {
                            databaseReference.removeEventListener(this)
                            var userFound = false
                            for (user in users.children) {
                                val userId = RudoChatInstance.getFirebaseAuth()?.uid.toString()
                                val currentUserId = user.key
                                if (userId == currentUserId) {
                                    userFound = true
                                    break
                                }
                            }
                            if (!userFound) {
                                databaseReference.child(RudoChatInstance.getFirebaseAuth()?.uid.toString())
                                    .child("userName")
                                    .setValue(RudoChatInstance.getFirebaseAuth()?.currentUser?.displayName)
                                    .addOnCompleteListener {
                                        databaseReference.child(RudoChatInstance.getFirebaseAuth()?.uid.toString())
                                            .child("profilePhoto")
                                            .setValue(DEFAULT_USER_PHOTO)
                                            .addOnCompleteListener {
                                                val result = ResultUserChat().apply {
                                                    success = true
                                                }
                                                trySend(result).isSuccess
                                            }
                                            .addOnFailureListener {
                                                val result = ResultUserChat().apply {
                                                    success = false
                                                    error = it
                                                }
                                                trySend(result).isSuccess
                                            }
                                    }
                                    .addOnFailureListener {
                                        val result = ResultUserChat().apply {
                                            success = false
                                            error = it
                                        }
                                        trySend(result).isSuccess
                                    }
                            } else {
                                val result = ResultUserChat().apply {
                                    success = true
                                    exists = true
                                }
                                trySend(result).isSuccess
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
                    awaitClose {}
                }
                BasicConfiguration.Type.BACK -> {
                }
                BasicConfiguration.Type.MIX -> {
                }
                BasicConfiguration.Type.USER_CONF -> {
                }
            }
        }
    }

    override fun initCurrentUserChats(): Flow<MutableList<Pair<String, String>>> {
        return callbackFlow {
            val listChatId = mutableListOf<Pair<String, String>>()
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(users: DataSnapshot) {
                    databaseReference.removeEventListener(this)
                    for (user in users.children) {
                        if (user.key != RudoChatInstance.getFirebaseAuth()?.uid) {
                            val chatId = generateId()
                            listChatId.add(Pair(user.key.toString(), chatId))
                            val chat = EmptyChat().apply {
                                lastMessage = ""
                                name = user.child("userName").value.toString()
                                otherUserId = user.key
                                otherUserImage = user.child("profilePhoto").value.toString()
                            }
                            databaseReference.child(RudoChatInstance.getFirebaseAuth()?.uid.toString())
                                .child("chats")
                                .child(chatId)
                                .setValue(chat)
                        }
                    }
                    trySend(listChatId).isSuccess
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
            awaitClose {}
        }
    }

    override fun initOtherUsersChats(listChatId: MutableList<Pair<String, String>>): Flow<ResultInfo> {
        return callbackFlow {
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(users: DataSnapshot) {
                    databaseReference.removeEventListener(this)
                    for (user in users.children) {
                        if (user.key != RudoChatInstance.getFirebaseAuth()?.uid.toString()) {
                            listChatId.getPair(user.key.toString())?.let { pair ->
                                val chat = EmptyChat().apply {
                                    lastMessage = ""
                                    name =
                                        RudoChatInstance.getFirebaseAuth()?.currentUser?.displayName
                                    otherUserId = RudoChatInstance.getFirebaseAuth()?.uid
                                    otherUserImage = DEFAULT_USER_PHOTO
                                }
                                databaseReference.child(user.key.toString())
                                    .child("chats")
                                    .child(pair.second)
                                    .setValue(chat)
                            }
                        }
                    }
                    val resultInfo = ResultInfo().apply {
                        success = true
                    }
                    trySend(resultInfo).isSuccess
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
            awaitClose {}
        }
    }

    override fun getChats(): Flow<MutableList<Chat>> {
        return callbackFlow {
            when (type) {
                BasicConfiguration.Type.FIREBASE -> {
                    val query =
                        databaseReference.child("${RudoChatInstance.getFirebaseAuth()?.uid}/chats")
                    val databaseListener =
                        query.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(chats: DataSnapshot) {
                                val chatList = mutableListOf<Chat>()
                                for (chat in chats.children) {
                                    val userChat = Chat().apply {
                                        id = chat.key
                                        name = chat.child("name").value.toString()
                                        otherUserId = chat.child("otherUserId").value.toString()
                                        otherUserImage =
                                            chat.child("otherUserImage").value.toString()
                                        lastMessage = chat.child("lastMessage").value.toString()
                                    }

                                    val messages = chat.child("messages")
                                    val maxMessages =
                                        if (messages.childrenCount <= LIMIT_MESSAGES) {
                                            LIMIT_MESSAGES
                                        } else {
                                            messages.childrenCount
                                        }
                                    val messagesList = mutableListOf<Message>()
                                    var count = 0
                                    for (message in messages.children) {
                                        if (count == maxMessages) {
                                            break
                                        }
                                        val messageObj = Message().apply {
                                            id = message.key
                                            text = message.child("text").value.toString()
                                            timestamp = message.child("timestamp").value as? Long
                                            userId = message.child("userId").value.toString()
                                        }
                                        messagesList.add(messageObj)
                                        count++
                                    }
                                    try {
                                        userChat.messages =
                                            messagesList.sorted() as MutableList<Message>
                                    } catch (ex: Exception) {
                                        userChat.messages = messagesList
                                    }
                                    chatList.add(userChat)
                                }
                                trySend(chatList).isSuccess
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }
                        })
                    awaitClose {
                        databaseReference.removeEventListener(databaseListener)
                    }
                }
                BasicConfiguration.Type.BACK -> {
                    trySend(ArrayList()).isSuccess
                }
                BasicConfiguration.Type.MIX -> {
                    trySend(ArrayList()).isSuccess
                }
                BasicConfiguration.Type.USER_CONF -> {
                    trySend(ArrayList()).isSuccess
                }
            }
        }
    }

    override fun getMessagesIndividual(chat: Chat, page: Int): Flow<MutableList<Message>> {
        return callbackFlow {
            when (type) {
                BasicConfiguration.Type.FIREBASE -> {
                    val correctPage = if (page >= 0) {
                        1
                    } else {
                        page
                    }
                    val newPage = correctPage * LIMIT_MESSAGES
                    val query =
                        databaseReference.child("${RudoChatInstance.getFirebaseAuth()?.uid}/chats/${chat.id}/messages")
                            .orderByChild("timestamp")
                            .limitToLast(newPage)
                    val databaseListener =
                        query.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(messages: DataSnapshot) {
                                val messagesList = mutableListOf<Message>()
                                for (message in messages.children) {
                                    val messageObj = Message().apply {
                                        id = message.key
                                        text = message.child("text").value.toString()
                                        timestamp = message.child("timestamp").value as? Long
                                        userId = message.child("userId").value.toString()
                                    }
                                    messagesList.add(messageObj)
                                }
                                trySend(messagesList).isSuccess
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }
                        })
                    awaitClose {
                        databaseReference.removeEventListener(databaseListener)
                    }
                }
                BasicConfiguration.Type.BACK -> {
                    trySend(ArrayList()).isSuccess
                }
                BasicConfiguration.Type.MIX -> {
                    trySend(ArrayList()).isSuccess
                }
                BasicConfiguration.Type.USER_CONF -> {
                    trySend(ArrayList()).isSuccess
                }
            }
        }
    }

    override fun getGroups(): Flow<MutableList<Group>> {
        return callbackFlow {
        }
    }

    override fun sendMessage(chatInfo: ChatInfo, message: Message): Flow<ResultInfo> {
        return callbackFlow {
            when (type) {
                BasicConfiguration.Type.FIREBASE -> {
                    val currentUserChat = databaseReference.child(chatInfo.userId.toString())
                        .child("chats")
                        .child(chatInfo.chatId.toString())
                    val otherUserChat = databaseReference.child(chatInfo.otherUserId.toString())
                        .child("chats")
                        .child(chatInfo.chatId.toString())
                    // Current user chat
                    currentUserChat.child("messages")
                        .push().setValue(message).addOnCompleteListener {
                            // Other user chat
                            otherUserChat.child("messages")
                                .push().setValue(message)
                                .addOnCompleteListener {
                                    // Update last message of both users
                                    currentUserChat.updateChildren(mapOf("lastMessage" to message.text))
                                        .addOnCompleteListener {
                                            otherUserChat.updateChildren(mapOf("lastMessage" to message.text))
                                                .addOnCompleteListener {
                                                    val result = ResultInfo().apply {
                                                        success = true
                                                    }
                                                    trySend(result).isSuccess
                                                }
                                                .addOnFailureListener {
                                                    val result = ResultInfo().apply {
                                                        success = false
                                                        error = it
                                                    }
                                                    trySend(result).isSuccess
                                                }
                                        }
                                        .addOnFailureListener {
                                            val result = ResultInfo().apply {
                                                success = false
                                                error = it
                                            }
                                            trySend(result).isSuccess
                                        }
                                }
                                .addOnFailureListener {
                                    val result = ResultInfo().apply {
                                        success = false
                                        error = it
                                    }
                                    trySend(result).isSuccess
                                }
                        }
                        .addOnFailureListener {
                            val result = ResultInfo().apply {
                                success = false
                                error = it
                            }
                            trySend(result).isSuccess
                        }
                    awaitClose {}
                }
                BasicConfiguration.Type.BACK -> {
                }
                BasicConfiguration.Type.MIX -> {
                }
                BasicConfiguration.Type.USER_CONF -> {
                }
            }
        }
    }
}
