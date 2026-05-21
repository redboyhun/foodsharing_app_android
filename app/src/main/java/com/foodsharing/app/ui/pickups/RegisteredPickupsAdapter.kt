package com.foodsharing.app.ui.pickups

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.foodsharing.app.data.model.PickupOption
import com.foodsharing.app.databinding.ItemRegisteredPickupBinding
import com.foodsharing.app.util.formatPickupDate

class RegisteredPickupsAdapter(
    private val currentUserId: Int,
    private val onLeaveClick: (PickupOption) -> Unit
) : ListAdapter<PickupOption, RegisteredPickupsAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemRegisteredPickupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pickup: PickupOption) {
            val isMySlotUnconfirmed = pickup.occupiedSlots.any { it.id == currentUserId } && pickup.isConfirmed == false
            binding.tvStoreName.text = if (isMySlotUnconfirmed) "${pickup.store.name} ⏳" else pickup.store.name
            binding.tvDate.text = formatPickupDate(pickup.date)
            binding.btnLeave.setOnClickListener { onLeaveClick(pickup) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRegisteredPickupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class DiffCallback : DiffUtil.ItemCallback<PickupOption>() {
        override fun areItemsTheSame(old: PickupOption, new: PickupOption) =
            old.store.id == new.store.id && old.date == new.date
        override fun areContentsTheSame(old: PickupOption, new: PickupOption) = old == new
    }
}
