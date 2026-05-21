package com.foodsharing.app.ui.conversations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.foodsharing.app.R
import com.foodsharing.app.data.model.Message
import com.foodsharing.app.databinding.ItemChatMessageBinding
import com.foodsharing.app.util.formatMessageTime

/**
 * UI model representing a chat message with its author's name already resolved.
 * This avoids calling notifyDataSetChanged() when profile information is updated separately.
 */
data class ChatMessageUiModel(
    val message: Message,
    val authorName: String?
)

class ChatAdapter(
    private val currentUserId: Int
) : ListAdapter<ChatMessageUiModel, ChatAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemChatMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(uiModel: ChatMessageUiModel) {
            val message = uiModel.message
            val isMe = message.authorId == currentUserId
            
            binding.tvAuthor.text = uiModel.authorName ?: "Unknown"
            binding.tvBody.text = message.body
            binding.tvTime.text = formatMessageTime(message.sentAt)
            
            val params = binding.cardMessage.layoutParams as ConstraintLayout.LayoutParams
            if (isMe) {
                params.horizontalBias = 1.0f
                binding.cardMessage.setCardBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.primary_container)
                )
                binding.tvAuthor.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.on_primary_container)
                )
                binding.tvBody.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.on_primary_container)
                )
                binding.tvTime.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.on_primary_container)
                )
            } else {
                params.horizontalBias = 0.0f
                binding.cardMessage.setCardBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.surface_pure)
                )
                binding.tvAuthor.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.primary)
                )
                binding.tvBody.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.on_surface)
                )
                binding.tvTime.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.text_muted)
                )
            }
            binding.cardMessage.layoutParams = params
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChatMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class DiffCallback : DiffUtil.ItemCallback<ChatMessageUiModel>() {
        override fun areItemsTheSame(old: ChatMessageUiModel, new: ChatMessageUiModel) = 
            old.message.id == new.message.id

        override fun areContentsTheSame(old: ChatMessageUiModel, new: ChatMessageUiModel) = 
            old == new
    }
}
