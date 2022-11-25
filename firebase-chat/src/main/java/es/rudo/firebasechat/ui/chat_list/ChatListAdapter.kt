package es.rudo.firebasechat.ui.chat_list

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import es.rudo.firebasechat.R
import es.rudo.firebasechat.databinding.ItemChatBinding
import es.rudo.firebasechat.models.Chat

class ChatListAdapter(
    private val clickListener: (Chat) -> Unit
) : ListAdapter<Chat, ChatListAdapter.ViewHolder>(ListAdapterCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class ViewHolder private constructor(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Chat,
            clickListener: (Chat) -> Unit
        ) {
            binding.chat = item
            binding.chatContainer.setOnClickListener {
                clickListener.invoke(item)
            }

            Glide.with(binding.imageUser.context)
                .load(item.otherUserImage)
                .placeholder(
                    ContextCompat.getDrawable(
                        binding.imageUser.context,
                        R.drawable.shape_gray_rounded_rectangle
                    )
                )
                .error(
                    ContextCompat.getDrawable(
                        binding.imageUser.context,
                        R.drawable.shape_gray_rounded_rectangle
                    )
                )
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        p0: GlideException?,
                        p1: Any?,
                        p2: Target<Drawable>?,
                        p3: Boolean
                    ): Boolean {
                        Log.d("_TAG_", "Fail: $p0")
                        return false
                    }

                    override fun onResourceReady(
                        p0: Drawable?,
                        p1: Any?,
                        p2: Target<Drawable>?,
                        p3: DataSource?,
                        p4: Boolean
                    ): Boolean {
                        Log.d("_TAG_", "Correct")
                        return false
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(GenericTransitionOptions.with(R.anim.fade_in))
                .into(binding.imageUser)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemChatBinding.inflate(layoutInflater, parent, false)
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
