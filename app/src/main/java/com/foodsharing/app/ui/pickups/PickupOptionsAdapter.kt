package com.foodsharing.app.ui.pickups

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.foodsharing.app.data.model.PickupOption
import com.foodsharing.app.databinding.ItemPickupOptionBinding

class PickupOptionsAdapter(
    private val onJoinClick: (PickupOption) -> Unit
) : ListAdapter<PickupOption, PickupOptionsAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemPickupOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(option: PickupOption) {
            binding.tvStoreName.text = option.store.name
            binding.tvDate.text = option.date.take(16).replace("T", " ")
            binding.tvSlots.text = "${option.occupiedSlots.size}/${option.slots} slots"
            binding.btnJoin.isEnabled = option.occupiedSlots.size < option.slots
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
