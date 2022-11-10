package es.rudo.firebasechat.ui.chat

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import es.rudo.firebasechat.R
import es.rudo.firebasechat.domain.models.Chat
import es.rudo.firebasechat.domain.models.Message
import es.rudo.firebasechat.databinding.ActivityChatBinding
import es.rudo.firebasechat.helpers.Constants.CHAT
import es.rudo.firebasechat.main.instance.RudoChatInstance

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: ChatAdapter

    private val viewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)

        binding.lifecycleOwner = this
        binding.activity = this
        binding.viewModel = viewModel

        setupToolbar()
        setupAdapter()
        initObservers()

        loadData()
        viewModel.getMessages()

        setupViews()
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
            RudoChatInstance.getFirebaseAuth()?.uid,
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
        binding.recycler.setHasFixedSize(false)
    }

    private fun initObservers() {
        //TODO revisar cuando haya paginación (cambiar al itemRange)
        viewModel.messageList.observe(this) { messages ->
            messages?.let {
                Log.d("TestSize", "Tamaño de la lista adapter ${adapter.currentList.size}")
                Log.d("TestSize", "Tamaño de la lista firebase ${it.size}")
//                if (adapter.currentList.size < it.size)  {
//                    val list = mutableListOf<Message>()
//                    viewModel.messageList.value?.let { it1 -> list.addAll(it1) }
                    adapter.submitList(it)
//                    binding.recycler.smoothScrollToPosition(it.lastIndex)
//                    adapter.notifyItemInserted(it.lastIndex)
//                }
            }
        }

        viewModel.sendMessageAttempt.observe(this) {
            //TODO revisar si hay que hacer el notify al adapter aquí
                Log.d("TEST", "Posición del adapter ${adapter.currentList.size}")
                Log.d("TEST", "Posición de la lista ${viewModel.messageList.value?.size}")
                if (it.isNotEmpty()) {
                    adapter.submitList(viewModel.messageList.value)
//                    binding.recycler.smoothScrollToPosition(it.lastIndex)
//                    adapter.notifyItemInserted(it.lastIndex)
                }
        }

        viewModel.sendMessageSuccess.observe(this) {
            //TODO controlar el cambio de estado de los mensajes y/o su siguiente intento de reenvio
        }

        viewModel.messageListHistoryUpdateStarted.observe(this) {
            // TODO controlará cuándo se iniciar la carga del historial (puede implicar cambios de UI)
        }

        viewModel.messageListHistoryUpdateFinished.observe(this) {
            // TODO controlará el puntero cuando se haya cargado la siguiente página del historial
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private fun loadData() {
        intent.extras?.let {
            if (it.containsKey(CHAT)) {
                (it.getSerializable(CHAT) as? Chat)?.let { chat ->
                    viewModel.chat = chat
                    adapter.submitList(chat.messages)
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
