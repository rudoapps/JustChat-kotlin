package es.rudo.firebasechat.ui.chat_list

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import es.rudo.firebasechat.R
import es.rudo.firebasechat.data.model.chats.Chat
import es.rudo.firebasechat.databinding.ActivityChatListBinding
import es.rudo.firebasechat.ui.chat.ChatActivity

@AndroidEntryPoint
class ChatListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatListBinding
    private val viewModel: ChatListViewModel by viewModels()
    private lateinit var adapter: ChatHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.activity_chat_list,
            null,
            false
        )
        setContentView(binding.root)

        binding.lifecycleOwner = this

        initAdapter()
        setUpObservables()

        viewModel.getChats()
    }

    private fun initAdapter() {
        adapter = ChatHistoryAdapter(this) {
            startActivity(
                Intent(this, ChatActivity::class.java)
                    .putExtra(ChatActivity.CHAT, it)
            )
        }
        binding.recyclerChats.adapter = adapter
    }

    private fun setUpObservables() {
        viewModel.chats.observe(this) {
            //adapter.submitList(it)
            adapter.submitList(
                listOf(
                    Chat().apply {
                        name = "Las quintillizas"
                        otherUserImage =
                            "https://i.pinimg.com/736x/d6/29/01/d62901573a2f7eebf88da077b086c02b.jpg"
                        lastMessage = "Este es el último mensaje"
                    }
                )
            )
        }
    }
}