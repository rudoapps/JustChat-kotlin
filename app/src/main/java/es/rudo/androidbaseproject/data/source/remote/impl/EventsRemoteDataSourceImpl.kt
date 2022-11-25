package es.rudo.androidbaseproject.data.source.remote.impl

import android.content.Context
import android.util.Log
import com.google.firebase.database.* // ktlint-disable no-wildcard-imports
import es.rudo.androidbaseproject.data.dto.EmptyChat
import es.rudo.androidbaseproject.data.dto.converters.toMessageBack
import es.rudo.androidbaseproject.data.source.remote.EventsRemoteDataSource
import es.rudo.androidbaseproject.helpers.Constants.DEFAULT_USER_PHOTO
import es.rudo.androidbaseproject.helpers.Constants.LIMIT_MESSAGES
import es.rudo.androidbaseproject.helpers.Constants.LIMIT_SIZE_ID
import es.rudo.androidbaseproject.ui.main.MainActivity
import es.rudo.firebasechat.helpers.extensions.getUserId
import es.rudo.firebasechat.models.*
import es.rudo.firebasechat.models.results.ResultInfo
import es.rudo.firebasechat.models.results.ResultUserChat
import generateId
import getPair
import getResult
import getResultUserChat
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject
import kotlin.Exception

class EventsRemoteDataSourceImpl @Inject constructor(
    private val databaseReference: DatabaseReference,
    private val context: Context
) : EventsRemoteDataSource {

    override fun initUser(deviceToken: String): Flow<ResultUserChat> {
        return channelFlow {
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(users: DataSnapshot) {
                    databaseReference.removeEventListener(this)
                    var userFound = false
                    for (user in users.children) {
                        val userId = context.getUserId().toString()
                        val currentUserId = user.key
                        if (userId == currentUserId) {
                            userFound = true
                            break
                        }
                    }
                    if (!userFound) {
                        databaseReference.child(context.getUserId().toString())
                            .child("userName")
                            .setValue(MainActivity.getFirebaseAuth()?.currentUser?.displayName)
                            .addOnCompleteListener {
                                databaseReference.child(context.getUserId().toString())
                                    .child("profilePhoto")
                                    .setValue(DEFAULT_USER_PHOTO)
                                    .addOnCompleteListener {
                                        databaseReference.child(
                                            context.getUserId().toString()
                                        )
                                            .child("deviceToken")
                                            .setValue(deviceToken)
                                            .addOnCompleteListener {
                                                trySend(getResultUserChat(isSuccess = true)).isSuccess
                                            }
                                    }
                                    .addOnFailureListener {
                                        trySend(
                                            getResultUserChat(
                                                isSuccess = true,
                                                exception = it
                                            )
                                        ).isFailure
                                    }
                            }
                            .addOnFailureListener {
                                trySend(
                                    getResultUserChat(isSuccess = true, exception = it)
                                ).isFailure
                            }
                    } else {
                        databaseReference.child(context.getUserId().toString())
                            .updateChildren(mapOf("deviceToken" to deviceToken))
                            .addOnCompleteListener {
                                trySend(
                                    getResultUserChat(
                                        isSuccess = true,
                                        exist = true
                                    )
                                ).isSuccess
                            }
                            .addOnFailureListener {
                                trySend(
                                    getResultUserChat(isSuccess = true, exception = it)
                                ).isFailure
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
            awaitClose {
                this.channel.close()
            }
        }
    }

    override fun initCurrentUserChats(): Flow<MutableList<Pair<String, String>>> {
        return channelFlow {
            val listChatId = mutableListOf<Pair<String, String>>()
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(users: DataSnapshot) {
                    databaseReference.removeEventListener(this)
                    for (user in users.children) {
                        if (user.key != context.getUserId()) {
                            val chatId =
                                "${System.currentTimeMillis()}-${generateId(LIMIT_SIZE_ID)}"
                            listChatId.add(Pair(user.key.toString(), chatId))
                            val chat = EmptyChat().apply {
                                lastMessage = ""
                                name = user.child("userName").value.toString()
                                otherUserId = user.key
                                otherUserImage = user.child("profilePhoto").value.toString()
                            }
                            databaseReference.child(context.getUserId().toString())
                                .child("chats")
                                .child(chatId)
                                .setValue(chat)
                        }
                        trySend(listChatId).isSuccess
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
            awaitClose {
                this.channel.close()
            }
        }
    }

    override fun initOtherUsersChats(listChatId: MutableList<Pair<String, String>>): Flow<ResultInfo> {
        return channelFlow {
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(users: DataSnapshot) {
                    databaseReference.removeEventListener(this)
                    for (user in users.children) {
                        if (user.key != context.getUserId().toString()) {
                            listChatId.getPair(user.key.toString())?.let { pair ->
                                val chat = EmptyChat().apply {
                                    lastMessage = ""
                                    name = MainActivity.getFirebaseAuth()?.currentUser?.displayName
                                    otherUserId = context.getUserId()
                                    otherUserImage = DEFAULT_USER_PHOTO
                                }
                                databaseReference.child(user.key.toString())
                                    .child("chats")
                                    .child(pair.second)
                                    .setValue(chat)
                            }
                        }
                    }
                    trySend(getResult(true)).isSuccess
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
            awaitClose {
                this.channel.close()
            }
        }
    }

    override fun getChats(): Flow<MutableList<Chat>> {
        return channelFlow {
            val query =
                databaseReference.child("${context.getUserId()}/chats")
            val databaseListener =
                query.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(chats: DataSnapshot) {
                        val chatList = mutableListOf<Chat>()
                        for (chat in chats.children) {
                            val messagesList = mutableListOf<ChatMessageItem>()
                            val userChat = Chat().apply {
                                id = chat.key
                                name = chat.child("name").value.toString()
                                otherUserId = chat.child("otherUserId").value.toString()
                                otherUserImage =
                                    chat.child("otherUserImage").value.toString()
                                lastMessage = chat.child("lastMessage").value.toString()
                                messages = messagesList
                            }

                            getDeviceToken(
                                userChat.otherUserId.toString(),
                                object : SourceListener {
                                    override fun listMessages(messages: MutableList<ChatMessageItem>) {
                                    }

                                    override fun deviceToken(deviceToken: String) {
                                        userChat.userDeviceToken = deviceToken
                                        getLastMessages(
                                            userChat.id.toString(),
                                            object : SourceListener {
                                                override fun listMessages(messages: MutableList<ChatMessageItem>) {
                                                    messagesList.addAll(messages)
                                                    chatList.add(userChat)
                                                    if (chatList.size == chats.children.count()) {
                                                        trySend(chatList).isSuccess
                                                    }
                                                }

                                                override fun deviceToken(deviceToken: String) {
                                                }
                                            }
                                        )
                                    }
                                }
                            )
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            try {
                awaitClose {
                    databaseReference.removeEventListener(databaseListener)
                    this.channel.close()
                }
            } catch (ex: Exception) {
                Log.e(javaClass.simpleName, ex.toString())
            }
        }
    }

    private fun getDeviceToken(userId: String, listener: SourceListener) {
        databaseReference.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(user: DataSnapshot) {
                databaseReference.removeEventListener(this)
                val deviceToken = user.child("deviceToken").value.toString()
                listener.deviceToken(deviceToken)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun getLastMessages(chatId: String, messageListener: SourceListener) {
        val query =
            databaseReference.child("${context.getUserId()}/chats/$chatId/messages")
                .orderByChild("serverTimestamp")
                .limitToLast(LIMIT_MESSAGES)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(messages: DataSnapshot) {
                databaseReference.removeEventListener(this)
                val messagesList = mutableListOf<ChatMessageItem>()
                for (message in messages.children) {
                    val messageObj = ChatMessageItem().apply {
                        id = message.key
                        text = message.child("text").value.toString()
                        timestamp = message.child("serverTimestamp").value as? Long
                        userId = message.child("userId").value.toString()
                    }
                    messagesList.add(messageObj)
                }
                messageListener.listMessages(messagesList)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun getCurrentUser(): Flow<UserData> {
        return channelFlow {
            var count = 0
            databaseReference.child("${context.getUserId()}")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(user: DataSnapshot) {
                        databaseReference.removeEventListener(this)
                        if (count == 0) {
                            count++
                            val userData = UserData().apply {
                                userId = user.key
                                userName = user.child("userName").value.toString()
                                userPhoto = user.child("profilePhoto").value.toString()
                                userDeviceToken = user.child("deviceToken").value.toString()
                            }
                            trySend(userData).isSuccess
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            awaitClose {
                this.channel.close()
            }
        }
    }

    override fun getMessagesIndividual(chat: Chat, page: Int): Flow<MutableList<ChatMessageItem>> {
        return channelFlow {
            val correctPage = if (page >= 0) {
                1
            } else {
                page
            }
            val newPage = correctPage * LIMIT_MESSAGES
            val query =
                databaseReference.child("${context.getUserId()}/chats/${chat.id}/messages")
                    .orderByChild("serverTimestamp")
//                            .limitToLast(newPage)
            val databaseListener =
                query.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(messages: DataSnapshot) {
                        val messagesList = mutableListOf<ChatMessageItem>()
                        for (message in messages.children) {
                            val messageObj = ChatMessageItem().apply {
                                id = message.key
                                text = message.child("text").value.toString()
                                timestamp = message.child("serverTimestamp").value as? Long
                                userId = message.child("userId").value.toString()
                            }
                            messagesList.add(messageObj)
                        }
                        trySend(messagesList).isSuccess
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            try {
                awaitClose {
                    databaseReference.removeEventListener(databaseListener)
                    this.channel.close()
                }
            } catch (ex: Exception) {
                Log.e(javaClass.simpleName, ex.toString())
            }
        }
    }

    override fun getGroups(): Flow<MutableList<Group>> {
        return callbackFlow {
        }
    }

    override fun sendMessage(chatInfo: ChatInfo, message: ChatMessageItem): Flow<ResultInfo> {
        return channelFlow {
            val messageId = message.id.toString()

            val backMessage = message.toMessageBack()
            backMessage.serverTimestamp = ServerValue.TIMESTAMP

            val currentUserChat = databaseReference.child(chatInfo.userId.toString())
                .child("chats")
                .child(chatInfo.chatId.toString())

            val otherUserChat = databaseReference.child(chatInfo.otherUserId.toString())
                .child("chats")
                .child(chatInfo.chatId.toString())

            // Current user chat
            currentUserChat.child("messages")
                .child(messageId).setValue(backMessage)
                .addOnCompleteListener {
                    // Other user chat
                    otherUserChat.child("messages")
                        .child(messageId).setValue(backMessage)
                        .addOnCompleteListener {
                            // Update last message of both users
                            currentUserChat.updateChildren(mapOf("lastMessage" to backMessage.text))
                                .addOnCompleteListener {
                                    otherUserChat.updateChildren(mapOf("lastMessage" to backMessage.text))
                                        .addOnCompleteListener {
                                            trySend(getResult(true)).isSuccess
                                        }
                                        .addOnFailureListener {
                                            trySend(getResult(false, it)).isFailure
                                        }
                                }
                                .addOnFailureListener {
                                    trySend(getResult(false, it)).isFailure
                                }
                        }
                        .addOnFailureListener {
                            trySend(getResult(false, it)).isFailure
                        }
                }
                .addOnFailureListener {
                    trySend(getResult(false, it)).isFailure
                }
            awaitClose {
                this.channel.close()
            }
        }
    }

    private interface SourceListener {
        fun listMessages(messages: MutableList<ChatMessageItem>)
        fun deviceToken(deviceToken: String)
    }
}
