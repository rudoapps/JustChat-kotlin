package es.rudo.firebasechat.adapters

import android.annotation.SuppressLint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
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

class ChatListAdapter(
    private val userId: String?, // TODO valorar setearlo en el companion object del activity y ahorrarse el param
    private val clickListener: MessageClickListener
) :
    ListAdapter<Message, ChatListAdapter.ViewHolder>(ListAdapterCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), userId, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Message,
            userId: String?,
            clickListener: MessageClickListener
        ) {
            binding.message = item
            binding.clickListener = clickListener
            binding.executePendingBindings()

            setMessageGravity(item.userId, userId)
            setMessageBackground()
        }

        private fun setMessageGravity(messageOwnerId: String?, userId: String?) {
            if (messageOwnerId == userId) {
                binding.text.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    startToStart = ConstraintLayout.LayoutParams.UNSET
                    endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                }
            } else {
                binding.text.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                    endToEnd = ConstraintLayout.LayoutParams.UNSET
                }
            }
        }

        private fun setMessageBackground() {
            // TODO valorar si es mejor crearla de cero o modificar un recurso ya existente
            val shape = ShapeDrawable(
                RoundRectShape(
                    floatArrayOf(40f, 40f, 40f, 40f, 40f, 40f, 40f, 40f),
                    null,
                    null
                )
            )
            shape.paint.color = ContextCompat.getColor(binding.root.context, R.color.teal_700)
            binding.viewBackground.background = shape
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
