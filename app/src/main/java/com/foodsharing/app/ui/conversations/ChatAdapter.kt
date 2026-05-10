package com.foodsharing.app.ui.conversations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.foodsharing.app.data.model.ChatMessage
import com.foodsharing.app.data.model.Profile
import com.foodsharing.app.databinding.ItemChatMessageBinding

class ChatAdapter(
    private val currentUserId: Int
) : ListAdapter<ChatMessage, ChatAdapter.ViewHolder>(DiffCallback()) {

    private var profiles: List<Profile> = emptyList()

    fun setProfiles(profiles: List<Profile>) {
        this.profiles = profiles
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemChatMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: ChatMessage) {
            val author = profiles.find { it.id == message.authorId }
            binding.tvAuthor.text = author?.name ?: message.authorName ?: "Unknown"
            binding.tvBody.text = message.body
            binding.tvTime.text = message.sentAt?.take(16)?.replace("T", " ") ?: ""
            
            // Basic alignment based on author
            val params = binding.root.layoutParams as? ViewGroup.MarginLayoutParams
            if (message.authorId == currentUserId) {
                params?.marginStart = 100
                params?.marginEnd = 0
            } else {
                params?.marginStart = 0
                params?.marginEnd = 100
            }
            binding.root.layoutParams = params
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChatMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class DiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(old: ChatMessage, new: ChatMessage) = old.id == new.id
        override fun areContentsTheSame(old: ChatMessage, new: ChatMessage) = old == new
    }
}
