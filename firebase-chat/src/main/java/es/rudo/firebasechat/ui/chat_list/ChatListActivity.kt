package es.rudo.firebasechat.ui.chat_list

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import es.rudo.firebasechat.R
import es.rudo.firebasechat.databinding.ActivityChatListBinding

class ChatListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatListBinding
    private val viewModel: ChatListViewModel by viewModels()
//    private lateinit var adapter: ChatListAdapter

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
    }

    private fun initAdapter() {
//        adapter = ChatListAdapter {
// //            val intent = Intent(this, ChatActivity::class.java)
// //            intent.putExtra(CHAT, it)
// //            startActivity(intent)
//        }
//        binding.recyclerChats.adapter = adapter
    }

    private fun setUpObservables() {
//        viewModel.chats.observe(this) {
//            adapter.submitList(it)
//            // TODO mock data
// //            adapter.submitList(
// //                listOf(
// //                    Chat().apply {
// //                        name = "Las quintillizas"
// //                        otherUserImage =
// //                            "https://i.pinimg.com/736x/d6/29/01/d62901573a2f7eebf88da077b086c02b.jpg"
// //                        lastMessage = "Este es el último mensaje"
// //                    }
// //                )
// //            )
//        }
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onResume() {
        super.onResume()
        // TODO replace with call obtain  chats
//        if (!viewModel.chats.value.isNullOrEmpty()) {
//            viewModel.getChats(isNetworkAvailable)
//        }
    }
}
