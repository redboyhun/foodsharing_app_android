package com.foodsharing.app.ui.pickups

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.foodsharing.app.data.model.RegisteredPickup
import com.foodsharing.app.databinding.ItemRegisteredPickupBinding

class RegisteredPickupsAdapter(
    private val onLeaveClick: (RegisteredPickup) -> Unit
) : ListAdapter<RegisteredPickup, RegisteredPickupsAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemRegisteredPickupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pickup: RegisteredPickup) {
            binding.tvStoreName.text = pickup.store.name
            binding.tvDate.text = pickup.date.take(16).replace("T", " ")
            binding.btnLeave.setOnClickListener { onLeaveClick(pickup) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRegisteredPickupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class DiffCallback : DiffUtil.ItemCallback<RegisteredPickup>() {
        override fun areItemsTheSame(old: RegisteredPickup, new: RegisteredPickup) =
            old.store.id == new.store.id && old.date == new.date
        override fun areContentsTheSame(old: RegisteredPickup, new: RegisteredPickup) = old == new
    }
}
