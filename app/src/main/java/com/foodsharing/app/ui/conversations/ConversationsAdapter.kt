package com.foodsharing.app.ui.conversations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.foodsharing.app.data.model.Conversation
import com.foodsharing.app.data.model.Profile
import com.foodsharing.app.databinding.ItemConversationBinding

class ConversationsAdapter(
    private val currentUserId: Int,
    private val onClick: (Conversation) -> Unit
) : ListAdapter<Conversation, ConversationsAdapter.ViewHolder>(DiffCallback()) {

    private var profiles: List<Profile> = emptyList()

    fun setProfiles(profiles: List<Profile>) {
        this.profiles = profiles
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemConversationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(conversation: Conversation) {
            // Title hierarchy: "title" then "username where userid not eq logged-in userid" then "Conversation"
            var title = conversation.title
            
            if (title.isNullOrBlank()) {
                val otherMemberId = conversation.members?.find { it != currentUserId }
                title = profiles.find { it.id == otherMemberId }?.name
            }
            
            binding.tvName.text = if (!title.isNullOrBlank()) title else "Conversation"
            binding.tvLastMessage.text = conversation.lastMessage?.body?.take(60) ?: ""
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

    class DiffCallback : DiffUtil.ItemCallback<Conversation>() {
        override fun areItemsTheSame(old: Conversation, new: Conversation) = old.id == new.id
        override fun areContentsTheSame(old: Conversation, new: Conversation) = old == new
    }
}
