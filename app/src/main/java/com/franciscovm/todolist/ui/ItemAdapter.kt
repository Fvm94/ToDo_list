package com.franciscovm.todolist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.franciscovm.todolist.data.Item
import com.franciscovm.todolist.databinding.ItemListItemBinding

class ItemAdapter() : ListAdapter<Item, ItemAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private val binding: ItemListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item){
            binding.apply {
                itemName.text = item.item
            }
        }

    }
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.item == newItem.item
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }
}