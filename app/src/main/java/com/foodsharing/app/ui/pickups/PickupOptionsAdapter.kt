package com.foodsharing.app.ui.pickups

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.foodsharing.app.R
import com.foodsharing.app.data.model.PickupOption
import com.foodsharing.app.databinding.ItemPickupOptionBinding
import com.foodsharing.app.util.formatPickupDate

class PickupOptionsAdapter(
    private val currentUserId: Int,
    private val onJoinClick: (PickupOption) -> Unit
) : ListAdapter<PickupOption, PickupOptionsAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemPickupOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(option: PickupOption) {
            val context = binding.root.context
            val isMySlotUnconfirmed = option.occupiedSlots.any { it.id == currentUserId } && option.isConfirmed == false
            
            binding.tvStoreName.text = if (isMySlotUnconfirmed) {
                context.getString(R.string.pickup_store_unconfirmed, option.store.name)
            } else {
                option.store.name
            }
            
            binding.tvDate.text = formatPickupDate(option.date)
            
            binding.tvSlots.text = context.getString(
                R.string.pickup_slots_format,
                option.occupiedSlots.size,
                option.slots
            )

            binding.btnJoin.isEnabled = option.occupiedSlots.size < option.slots && option.occupiedSlots.none { it.id == currentUserId }
            binding.btnJoin.setOnClickListener { onJoinClick(option) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPickupOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class DiffCallback : DiffUtil.ItemCallback<PickupOption>() {
        override fun areItemsTheSame(old: PickupOption, new: PickupOption) =
            old.store.id == new.store.id && old.date == new.date
        override fun areContentsTheSame(old: PickupOption, new: PickupOption) = old == new
    }
}
