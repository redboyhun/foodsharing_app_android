package com.foodsharing.app.ui.baskets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.foodsharing.app.data.model.Basket
import com.foodsharing.app.databinding.ItemBasketBinding

class BasketAdapter(
    private val onItemClick: (Basket) -> Unit,
    private val onLongClick: ((Basket) -> Unit)? = null
) : ListAdapter<Basket, BasketAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemBasketBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(basket: Basket) {
            binding.tvDescription.text = basket.description.take(80)
            binding.tvCreator.text = basket.creator?.name ?: ""
            binding.tvDistance.text = basket.distance?.let { "%.1f km".format(it) } ?: ""

            binding.root.setOnClickListener { onItemClick(basket) }
            onLongClick?.let { listener ->
                binding.root.setOnLongClickListener {
                    listener(basket)
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBasketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Basket>() {
        override fun areItemsTheSame(oldItem: Basket, newItem: Basket) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Basket, newItem: Basket) = oldItem == newItem
    }
}
