package es.rudo.firebasechat.ui.chat

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import es.rudo.firebasechat.R
import es.rudo.firebasechat.adapters.ChatListAdapter
import es.rudo.firebasechat.data.model.chats.Chat
import es.rudo.firebasechat.data.model.chats.ChatInfo
import es.rudo.firebasechat.data.model.chats.Message
import es.rudo.firebasechat.databinding.ActivityChatBinding
import es.rudo.firebasechat.main.instance.RudoChatInstance

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var adapter: ChatListAdapter
    private lateinit var chat: Chat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)

        binding.lifecycleOwner = this
        binding.activity = this
        binding.viewModel = viewModel

        setupToolbar()
        setupAdapter()
        initObservers()
        initListeners()

//        viewModel.loadMessageList()
        viewModel.getChats()
//        viewModel.initChat(Chat())
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setupAdapter() {
        adapter = ChatListAdapter(
            RudoChatInstance.getFirebaseAuth()?.uid,
            object : ChatListAdapter.MessageClickListener {
                override fun onClick(item: Message) {
                    // TODO
                }

                override fun onLongClick(item: Message) {
                    // TODO
                }
            }
        )

        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
            reverseLayout = false
        }
        binding.recycler.setHasFixedSize(false)
    }

    private fun initObservers() {
        // TODO valorar eliminar esta variable y convertir la lista a livedata controlando que la lista del adapter esté vacía
        viewModel.initialMessageListLoaded.observe(this) {
            it?.let { listLoaded ->
                if (listLoaded) {
                    adapter.submitList(viewModel.messageList)
                }
            }
        }

        viewModel.chats.observe(this) { chats ->
            setupChat(chats)
        }

        viewModel.messages.observe(this) { messages ->
            adapter.submitList(messages)
        }

        viewModel.messageSent.observe(this) {
            binding.editText.setText("")
        }

        viewModel.newMessageAddedToList.observe(this) {
            it?.let { messageAdded ->
                if (messageAdded) {
                    adapter.notifyItemInserted(viewModel.messageList.lastIndex)
                }
            }
        }

        viewModel.messageListHistoryUpdateStarted.observe(this) {
            // TODO controlará cuándo se iniciar la carga del historial (puede implicar cambios de UI)
        }

        viewModel.messageListHistoryUpdateFinished.observe(this) {
            // TODO controlará el puntero cuando se haya cargado la siguiente página del historial
        }
    }

    private fun initListeners() {
        binding.imageSend.setOnClickListener {
            RudoChatInstance.getFirebaseAuth()?.uid?.let { userId ->
                if (binding.editText.text.toString().isNotEmpty()) {
                    val message = Message()
                    message.userId = userId
                    message.text = binding.editText.text.toString()
                    message.timestamp = System.currentTimeMillis()
                    val chatInfo = ChatInfo()
                    chatInfo.chatId = chat.id
                    chatInfo.userId = userId
                    chatInfo.otherUserId = chat.otherUserId
                    viewModel.sendMessage(chatInfo, message)
                }
            }
        }
    }

    private fun setupChat(chats: MutableList<Chat>) {
        if (!chats.isNullOrEmpty()) {
            chat = chats[0]
            binding.textUser.text = chat.name
            Glide.with(this).load(chat.otherUserImage).into(binding.imageUser)
            viewModel.getMessages(chat)
        }
    }

    private fun closeSessionAndFinish() {
        RudoChatInstance.getOnTapClient()?.signOut()
            ?.addOnCompleteListener {
                Toast.makeText(
                    this,
                    getString(R.string.correct),
                    Toast.LENGTH_SHORT
                ).show()
            }
            ?.addOnFailureListener {
                Toast.makeText(
                    this,
                    getString(R.string.error_closing_session),
                    Toast.LENGTH_SHORT
                ).show()
            }
        finish()
    }

    override fun onBackPressed() {
        closeSessionAndFinish()
    }
}
