package es.rudo.justchat.ui.chat_list

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import es.rudo.justchat.R
import es.rudo.justchat.databinding.ActivityChatListBinding
import es.rudo.justchat.helpers.Constants.CHAT
import es.rudo.justchat.ui.chat.ChatActivity

class ChatListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatListBinding
    private lateinit var viewModel: ChatListViewModel
    private lateinit var adapter: ChatListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.activity_chat_list,
            null,
            false
        )
        viewModel = ViewModelProvider(this)[ChatListViewModel::class.java]
        setContentView(binding.root)

        binding.lifecycleOwner = this

        initAdapter()
        setUpObservables()

        viewModel.getChats()
    }

    private fun initAdapter() {
        adapter = ChatListAdapter {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra(CHAT, it)
            startActivity(intent)
        }
        binding.recyclerChats.adapter = adapter
    }

    private fun setUpObservables() {
        viewModel.chats.observe(this) {
            if (!it.isNullOrEmpty()) {
                adapter.submitList(it)
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onResume() {
        super.onResume()
        // TODO replace with call obtain  chats
        if (!viewModel.chats.value.isNullOrEmpty()) {
            viewModel.getChats()
        }
    }
}
