package es.rudo.justchat.ui.chat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.resources.TextAppearance
import es.rudo.justchat.R
import es.rudo.justchat.databinding.ItemDateBinding
import es.rudo.justchat.databinding.ItemMessageBinding
import es.rudo.justchat.helpers.Constants.DATE
import es.rudo.justchat.helpers.Constants.MESSAGE
import es.rudo.justchat.helpers.extensions.dpToPx
import es.rudo.justchat.helpers.extensions.getTime
import es.rudo.justchat.models.ChatBaseItem
import es.rudo.justchat.models.ChatDateItem
import es.rudo.justchat.models.ChatMessageItem

class ChatAdapter(
    private val userId: String?, // TODO valorar setearlo en el companion object del activity y ahorrarse el param
    private val clickListener: MessageClickListener
) : ListAdapter<ChatBaseItem, RecyclerView.ViewHolder>(ListAdapterCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ChatMessageItem -> MESSAGE
            else -> DATE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MESSAGE -> MessageViewHolder.from(parent)
            else -> DateViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return when (getItemViewType(position)) {
            MESSAGE -> (holder as MessageViewHolder).bind(
                getItem(position) as ChatMessageItem,
                userId,
                clickListener
            )
            else -> (holder as DateViewHolder).bind(getItem(position) as ChatDateItem)
        }
    }

    class DateViewHolder private constructor(private val binding: ItemDateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChatDateItem) {
            binding.textDate.text = item.date
        }

        companion object {
            fun from(parent: ViewGroup): DateViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemDateBinding.inflate(layoutInflater, parent, false)
                return DateViewHolder(binding)
            }
        }
    }

    class MessageViewHolder private constructor(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val textAppearance: TextAppearance? = null
        private val outMsgColor: Int = ContextCompat.getColor(binding.root.context, R.color.purple_200)
        private val inMsgColor: Int = ContextCompat.getColor(binding.root.context, R.color.teal_700)
        private val outMsgPaddingDp: Int = 40
        private val inMsgPaddingDp: Int = 40
        private val showMsgTime: Boolean = true

        fun bind(
            item: ChatMessageItem,
            userId: String?,
            clickListener: MessageClickListener
        ) {
            if (item.userId == userId) {
                setOutgoingMessageGravity(item.position)
                setOutgoingMessageBackground(item.position)
            } else {
                setIncomingMessageGravity(item.position)
                setIncomingMessageBackground(item.position)
            }

            setMessageStyle()
            setMessageContent(item.text, item.timestamp)
        }

        private fun setOutgoingMessageGravity(position: ChatMessageItem.MessagePosition?) {
            binding.layout.updateLayoutParams<RecyclerView.LayoutParams> {
                marginStart = binding.root.context.dpToPx(outMsgPaddingDp)
                marginEnd = when (position) {
                    ChatMessageItem.MessagePosition.TOP, ChatMessageItem.MessagePosition.MIDDLE -> {
                        binding.root.context.dpToPx(10)
                    }
                    else -> 0
                }
                topMargin = when (position) {
                    ChatMessageItem.MessagePosition.BOTTOM, ChatMessageItem.MessagePosition.MIDDLE -> {
                        binding.root.context.dpToPx(4)
                    }
                    else -> binding.root.context.dpToPx(8)
                }
            }
            binding.textMessage.updateLayoutParams<ConstraintLayout.LayoutParams> {
                startToStart = ConstraintLayout.LayoutParams.UNSET
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            }
            binding.textMessage.updatePadding(
                left = binding.root.context.dpToPx(10),
                right = when (position) {
                    ChatMessageItem.MessagePosition.BOTTOM, ChatMessageItem.MessagePosition.SINGLE -> {
                        binding.root.context.dpToPx(20)
                    }
                    else -> binding.root.context.dpToPx(10)
                }
            )
            binding.textTimestamp.updateLayoutParams<ConstraintLayout.LayoutParams> {
                marginStart = 0
                marginEnd = when (position) {
                    ChatMessageItem.MessagePosition.BOTTOM, ChatMessageItem.MessagePosition.SINGLE -> {
                        binding.root.context.dpToPx(10)
                    }
                    else -> 0
                }
            }
        }

        private fun setIncomingMessageGravity(position: ChatMessageItem.MessagePosition?) {
            binding.layout.updateLayoutParams<RecyclerView.LayoutParams> {
                marginStart = when (position) {
                    ChatMessageItem.MessagePosition.TOP, ChatMessageItem.MessagePosition.MIDDLE -> {
                        binding.root.context.dpToPx(10)
                    }
                    else -> 0
                }
                marginEnd = binding.root.context.dpToPx(inMsgPaddingDp)
                topMargin = when (position) {
                    ChatMessageItem.MessagePosition.BOTTOM, ChatMessageItem.MessagePosition.MIDDLE -> {
                        binding.root.context.dpToPx(4)
                    }
                    else -> binding.root.context.dpToPx(8)
                }
            }
            binding.textMessage.updateLayoutParams<ConstraintLayout.LayoutParams> {
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.UNSET
            }
            binding.textMessage.updatePadding(
                left = when (position) {
                    ChatMessageItem.MessagePosition.BOTTOM, ChatMessageItem.MessagePosition.SINGLE -> {
                        binding.root.context.dpToPx(20)
                    }
                    else -> binding.root.context.dpToPx(10)
                },
                right = binding.root.context.dpToPx(10)
            )
            binding.textTimestamp.updateLayoutParams<ConstraintLayout.LayoutParams> {
                marginStart = when (position) {
                    ChatMessageItem.MessagePosition.BOTTOM, ChatMessageItem.MessagePosition.SINGLE -> {
                        binding.root.context.dpToPx(10)
                    }
                    else -> 0
                }
                marginEnd = binding.root.context.dpToPx(4)
            }
        }

        private fun setOutgoingMessageBackground(position: ChatMessageItem.MessagePosition?) {
            val drawable = when (position) {
                ChatMessageItem.MessagePosition.TOP -> ContextCompat.getDrawable(binding.root.context, R.drawable.background_chat_top)
                ChatMessageItem.MessagePosition.MIDDLE -> ContextCompat.getDrawable(binding.root.context, R.drawable.background_chat_middle)
                ChatMessageItem.MessagePosition.BOTTOM -> ContextCompat.getDrawable(binding.root.context, R.drawable.background_chat_bottom)
                else -> ContextCompat.getDrawable(binding.root.context, R.drawable.background_chat_single)
            }

            drawable?.setTint(outMsgColor)
            binding.viewBackground.background = drawable
        }

        private fun setIncomingMessageBackground(position: ChatMessageItem.MessagePosition?) {
            val drawable = when (position) {
                ChatMessageItem.MessagePosition.TOP -> ContextCompat.getDrawable(binding.root.context, R.drawable.background_chat_left_top)
                ChatMessageItem.MessagePosition.MIDDLE -> ContextCompat.getDrawable(binding.root.context, R.drawable.background_chat_left_middle)
                ChatMessageItem.MessagePosition.BOTTOM -> ContextCompat.getDrawable(binding.root.context, R.drawable.background_chat_left_bottom)
                else -> ContextCompat.getDrawable(binding.root.context, R.drawable.background_chat_left_single)
            }

            drawable?.setTint(inMsgColor)
            binding.viewBackground.background = drawable
        }

        private fun setMessageStyle() {
        }

        private fun setMessageContent(message: String?, timestamp: Long?) {
            if (showMsgTime) {
                binding.textMessage.text = message?.padEnd(message.length + 10, '\u00A0')
                binding.textTimestamp.text = timestamp.getTime()
            } else {
                binding.textMessage.text = message
                binding.textTimestamp.text = ""
            }
        }

        companion object {
            fun from(parent: ViewGroup): MessageViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMessageBinding.inflate(layoutInflater, parent, false)
                return MessageViewHolder(binding)
            }
        }
    }

    class ListAdapterCallback : DiffUtil.ItemCallback<ChatBaseItem>() {
        override fun areItemsTheSame(oldItem: ChatBaseItem, newItem: ChatBaseItem): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ChatBaseItem, newItem: ChatBaseItem): Boolean {
            return oldItem == newItem
        }
    }

    interface MessageClickListener {
        fun onClick(item: ChatMessageItem)
        fun onLongClick(item: ChatMessageItem)
    }
}
