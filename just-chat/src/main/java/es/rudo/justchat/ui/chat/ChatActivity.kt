package es.rudo.justchat.ui.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.Glide
import es.rudo.justchat.R
import es.rudo.justchat.databinding.ActivityChatBinding
import es.rudo.justchat.helpers.Constants.CHAT
import es.rudo.justchat.helpers.utils.userId
import es.rudo.justchat.main.instance.JustChat
import es.rudo.justchat.models.Chat
import es.rudo.justchat.models.ChatMessageItem

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: ChatAdapter

    private lateinit var viewModel: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setContentView(this, R.layout.activity_chat)
        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]

        binding.lifecycleOwner = this
        binding.activity = this
        binding.viewModel = viewModel

        setupToolbar()
        setupAdapter()
        initObservers()
        initListeners()

        loadData()

        setupViews()
    }

    override fun onResume() {
        super.onResume()
        JustChat.appPreferences?.chatId = viewModel.chat?.id.toString()
    }

    override fun onPause() {
        super.onPause()
        JustChat.appPreferences?.chatId = ""
    }

    override fun onDestroy() {
        super.onDestroy()
        JustChat.appPreferences?.chatId = ""
    }

    override fun onBackPressed() {
        finish()
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
        adapter = ChatAdapter(
            userId,
            object : ChatAdapter.MessageClickListener {
                override fun onClick(item: ChatMessageItem) {
                    // TODO
                }

                override fun onLongClick(item: ChatMessageItem) {
                    // TODO
                }
            }
        )

        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
            reverseLayout = false
        }
        val itemAnimator = DefaultItemAnimator().apply { supportsChangeAnimations = false }
        binding.recycler.itemAnimator = itemAnimator
        binding.recycler.setHasFixedSize(false)
    }

    private fun initObservers() {
        // TODO revisar cuando haya paginación (cambiar al itemRange)
        viewModel.messageList.observe(this) { messages ->
            messages?.let {
                adapter.submitList(messages)
            }
        }

        viewModel.newMessageReceived.observe(this) {
            viewModel.messageList.value?.let { messages ->
                if (messages.isNotEmpty()) {
                    adapter.submitList(messages)

                    if ((binding.recycler.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() == adapter.itemCount - 2) {
                        adapter.itemCount.takeIf { it > 0 }?.let {
                            binding.recycler.scrollToPosition(it - 1)
                        }
                    } else {
                        adapter.notifyItemInserted(adapter.itemCount - 1)
                    }
                }
            }
        }

        viewModel.sendMessageAttempt.observe(this) {
            viewModel.messageList.value?.let { messages ->
                if (messages.isNotEmpty()) {
                    adapter.submitList(messages)
                    adapter.itemCount.takeIf { it > 0 }?.let {
                        binding.recycler.scrollToPosition(it - 1)
                    }
                }
                viewModel.sendNotification()
            }
        }

        viewModel.sendMessageSuccess.observe(this) {
            // TODO controlar el cambio de estado de los mensajes y/o su siguiente intento de reenvio
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
            viewModel.prepareMessageForSending(userId)
        }
    }

    private fun loadData() {
        viewModel.userId = userId
        intent.extras?.let {
            if (it.containsKey(CHAT)) {
                (it.getSerializable(CHAT) as? Chat)?.let { chat ->
                    viewModel.chat = chat
                    JustChat.appPreferences?.chatId = chat.id.toString()
                    viewModel.initFlowReceiveMessage()
                    viewModel.getMessageHistory(chat.messages)
                }
            }
        }
    }

    private fun setupViews() {
        binding.textUser.text = viewModel.chat?.name
        Glide.with(this)
            .load(viewModel.chat?.otherUserImage)
            .into(binding.imageUser)
    }
}
