package es.rudo.justchat.ui.chat

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.graphics.fonts.Font
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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
        private val outMsgDrawable: Drawable? = ContextCompat.getDrawable(binding.root.context, R.drawable.background_chat)
        private val inMsgDrawable: Drawable? = ContextCompat.getDrawable(binding.root.context, R.drawable.background_chat_left)
        private val outMsgColor: Int =
            ContextCompat.getColor(binding.root.context, R.color.purple_200)
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
                setOutgoingMessageGravity()
                setOutgoingMessageBackground()
            } else {
                setIncomingMessageGravity()
                setIncomingMessageBackground()
            }

            setMessageStyle()
            setMessageContent(item.text, item.timestamp)
        }

        private fun setOutgoingMessageGravity() {
            binding.layout.updateLayoutParams<RecyclerView.LayoutParams> {
                marginStart = binding.root.context.dpToPx(outMsgPaddingDp)
                marginEnd = 0
            }
            binding.textMessage.updateLayoutParams<ConstraintLayout.LayoutParams> {
                startToStart = ConstraintLayout.LayoutParams.UNSET
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            }
            binding.textMessage.updatePadding(
                left = binding.root.context.dpToPx(10),
                right = binding.root.context.dpToPx(20)
            )
            binding.textTimestamp.updateLayoutParams<ConstraintLayout.LayoutParams> {
                marginStart = 0
                marginEnd = binding.root.context.dpToPx(10)
            }
        }

        private fun setIncomingMessageGravity() {
            binding.layout.updateLayoutParams<RecyclerView.LayoutParams> {
                marginStart = 0
                marginEnd = binding.root.context.dpToPx(inMsgPaddingDp)
            }
            binding.textMessage.updateLayoutParams<ConstraintLayout.LayoutParams> {
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.UNSET
            }
            binding.textMessage.updatePadding(
                left = binding.root.context.dpToPx(20),
                right = binding.root.context.dpToPx(10)
            )
            binding.textTimestamp.updateLayoutParams<ConstraintLayout.LayoutParams> {
                marginStart = binding.root.context.dpToPx(10)
                marginEnd = 0
            }
        }

        private fun setOutgoingMessageBackground() {
            outMsgDrawable?.setTint(outMsgColor)
            binding.viewBackground.background = outMsgDrawable
        }

        private fun setIncomingMessageBackground() {
            inMsgDrawable?.setTint(inMsgColor)
            binding.viewBackground.background = inMsgDrawable
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
