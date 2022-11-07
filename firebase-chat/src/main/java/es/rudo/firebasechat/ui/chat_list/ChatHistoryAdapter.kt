package es.rudo.firebasechat.ui.chat_list

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import es.rudo.firebasechat.R
import es.rudo.firebasechat.domain.models.Chat
import es.rudo.firebasechat.databinding.ItemChatListBinding

class ChatHistoryAdapter(
    private val context: Context,
    private val clickListener: (Chat) -> Unit
) : ListAdapter<Chat, ChatHistoryAdapter.ViewHolder>(ListAdapterCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), context, clickListener)
    }

    class ViewHolder private constructor(private val binding: ItemChatListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Chat,
            context: Context,
            clickListener: (Chat) -> Unit
        ) {
            binding.chat = item
            binding.chatContainer.setOnClickListener {
                clickListener.invoke(item)
            }

            Glide.with(context)
                .load(item.otherUserImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imageUser)
                .onLoadFailed(ContextCompat.getDrawable(context, R.color.purple_700))
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemChatListBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class ListAdapterCallback : DiffUtil.ItemCallback<Chat>() {
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem == newItem
        }
    }
}
