package es.rudo.firebasechat.ui.chat

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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.rudo.firebasechat.R
import es.rudo.firebasechat.domain.models.Message
import es.rudo.firebasechat.databinding.ItemChatBinding
import es.rudo.firebasechat.helpers.extensions.dpToPx
import es.rudo.firebasechat.helpers.extensions.getTime

class ChatAdapter(
    private val userId: String?, // TODO valorar setearlo en el companion object del activity y ahorrarse el param
    private val clickListener: MessageClickListener
) :
    ListAdapter<Message, ChatAdapter.ViewHolder>(ListAdapterCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), userId, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val textFont: Font? = null
        private val textSize: Int? = null
        private val outMsgDrawable: Drawable? = null
        private val inMsgDrawable: Drawable? = null
        private val outMsgColor: Int = ContextCompat.getColor(binding.root.context, R.color.purple_200)
        private val inMsgColor: Int = ContextCompat.getColor(binding.root.context, R.color.teal_700)
        private val outMsgPaddingDp: Int = 40
        private val inMsgPaddingDp: Int = 40
        private val showMsgTime: Boolean = true

        fun bind(
            item: Message,
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
        }

        private fun setOutgoingMessageBackground() {
            // TODO valorar si es mejor crearla de cero o modificar un recurso ya existente
            val shape = ShapeDrawable(
                RoundRectShape(
                    floatArrayOf(40f, 40f, 40f, 40f, 0f, 0f, 40f, 40f),
                    null,
                    null
                )
            )
            shape.paint.color = outMsgColor
            binding.viewBackground.background = shape
        }

        private fun setIncomingMessageBackground() {
            // TODO valorar si es mejor crearla de cero o modificar un recurso ya existente
            val shape = ShapeDrawable(
                RoundRectShape(
                    floatArrayOf(40f, 40f, 40f, 40f, 40f, 40f, 0f, 0f),
                    null,
                    null
                )
            )
            shape.paint.color = inMsgColor
            binding.viewBackground.background = shape
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
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemChatBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class ListAdapterCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.userId == newItem.userId
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }
    }

    interface MessageClickListener {
        fun onClick(item: Message)
        fun onLongClick(item: Message)
    }
}
