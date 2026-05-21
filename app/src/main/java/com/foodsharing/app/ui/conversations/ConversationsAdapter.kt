package com.foodsharing.app.ui.conversations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.foodsharing.app.data.model.Conversation
import com.foodsharing.app.databinding.ItemConversationBinding
import com.foodsharing.app.util.formatMessageTime

/**
 * UI model representing a conversation with its resolved display title.
 */
data class ConversationUiModel(
    val conversation: Conversation,
    val displayTitle: String
)

class ConversationsAdapter(
    private val currentUserId: Int,
    private val onClick: (Conversation) -> Unit
) : ListAdapter<ConversationUiModel, ConversationsAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemConversationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(uiModel: ConversationUiModel) {
            val conversation = uiModel.conversation
            
            binding.tvName.text = uiModel.displayTitle
            binding.tvLastMessage.text = conversation.lastMessage?.body?.take(60) ?: ""
            binding.tvTimestamp.text = formatMessageTime(conversation.lastMessage?.sentAt)
            binding.unreadIndicator.visibility =
                if (conversation.unreadMessages > 0) android.view.View.VISIBLE else android.view.View.GONE
            binding.root.setOnClickListener { onClick(conversation) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemConversationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class DiffCallback : DiffUtil.ItemCallback<ConversationUiModel>() {
        override fun areItemsTheSame(old: ConversationUiModel, new: ConversationUiModel) = 
            old.conversation.id == new.conversation.id
            
        override fun areContentsTheSame(old: ConversationUiModel, new: ConversationUiModel) = 
            old == new
    }
}
