package es.rudo.firebasechat.ui.chat_list

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import es.rudo.firebasechat.R
import es.rudo.firebasechat.data.dto.Notification
import es.rudo.firebasechat.databinding.ActivityChatListBinding
import es.rudo.firebasechat.helpers.Constants.CHAT
import es.rudo.firebasechat.helpers.extensions.isNetworkAvailable
import es.rudo.firebasechat.main.instance.JustChat
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

        viewModel.sendNotification(
            Notification(
                "titleExample",
                "messageDescription",
                JustChat.getFirebaseAuth()?.uid.toString()
            )
        )

//        viewModel.initUser(isNetworkAvailable)
    }

    private fun initAdapter() {
        adapter = ChatHistoryAdapter(this) {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra(CHAT, it)
            startActivity(intent)
        }
        binding.recyclerChats.adapter = adapter
    }

    private fun setUpObservables() {
        viewModel.chats.observe(this) {
            adapter.submitList(it)
            // TODO mock data
//            adapter.submitList(
//                listOf(
//                    Chat().apply {
//                        name = "Las quintillizas"
//                        otherUserImage =
//                            "https://i.pinimg.com/736x/d6/29/01/d62901573a2f7eebf88da077b086c02b.jpg"
//                        lastMessage = "Este es el último mensaje"
//                    }
//                )
//            )
        }

        viewModel.userInitialized.observe(this) {
            if (it.success == false) {
                Toast.makeText(this, it.error?.message.toString(), Toast.LENGTH_SHORT).show()
            } else {
                if (it.exists == true) {
                    viewModel.getChats(isNetworkAvailable)
                } else {
                    viewModel.initCurrentUserChats(isNetworkAvailable)
                }
            }
        }

        viewModel.listChatId.observe(this) {
            if (it.isNotEmpty()) {
                viewModel.initOtherUsersChats(isNetworkAvailable, it)
            }
        }

        viewModel.chatsInitialized.observe(this) {
            if (it.success == false) {
                Toast.makeText(this, it.error?.message.toString(), Toast.LENGTH_SHORT).show()
            } else {
                viewModel.getChats(isNetworkAvailable)
            }
        }
    }

    override fun onBackPressed() {
//        closeSessionAndFinish()
        finish()
    }

    private fun closeSessionAndFinish() {
        JustChat.getOnTapClient()?.signOut()
            ?.addOnCompleteListener {
                JustChat.getFirebaseAuth()?.signOut()
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
}
