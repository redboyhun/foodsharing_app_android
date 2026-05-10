package com.foodsharing.app.ui.conversations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.foodsharing.app.data.model.Message
import com.foodsharing.app.data.model.Profile
import com.foodsharing.app.databinding.ItemChatMessageBinding
import com.foodsharing.app.util.formatMessageTime

class ChatAdapter(
    private val currentUserId: Int
) : ListAdapter<Message, ChatAdapter.ViewHolder>(DiffCallback()) {

    private var profiles: List<Profile> = emptyList()

    fun setProfiles(profiles: List<Profile>) {
        this.profiles = profiles
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemChatMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            val author = profiles.find { it.id == message.authorId }
            binding.tvAuthor.text = author?.name ?: "Unknown"
            binding.tvBody.text = message.body
            binding.tvTime.text = formatMessageTime(message.sentAt)
            
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

    class DiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(old: Message, new: Message) = old.id == new.id
        override fun areContentsTheSame(old: Message, new: Message) = old == new
    }
}
