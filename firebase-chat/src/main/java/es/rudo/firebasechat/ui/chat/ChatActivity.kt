package es.rudo.firebasechat.ui.chat

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import es.rudo.firebasechat.R
import es.rudo.firebasechat.databinding.ActivityChatBinding
import es.rudo.firebasechat.domain.models.Chat
import es.rudo.firebasechat.domain.models.Message
import es.rudo.firebasechat.helpers.Constants.CHAT
import es.rudo.firebasechat.helpers.extensions.isNetworkAvailable
import es.rudo.firebasechat.main.instance.JustChat

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: ChatAdapter

    private val viewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setContentView(this, R.layout.activity_chat)

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
            JustChat.getFirebaseAuth()?.uid,
            object : ChatAdapter.MessageClickListener {
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
        (binding.recycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
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

    private fun loadData() {
        intent.extras?.let {
            if (it.containsKey(CHAT)) {
                (it.getSerializable(CHAT) as? Chat)?.let { chat ->
                    viewModel.chat = chat
                    viewModel.getMessages(chat.messages)
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
