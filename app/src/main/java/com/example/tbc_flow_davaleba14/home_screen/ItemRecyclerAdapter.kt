package com.example.tbc_flow_davaleba14.home_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tbc_flow_davaleba14.databinding.ItemRecyclerElementBinding

class ItemRecyclerAdapter :
    ListAdapter<Item, ItemRecyclerAdapter.ItemRecyclerViewHolder>(ItemDiffUtil()) {

    private lateinit var listener: OnItemClickListener

    interface OnItemClickListener{
        fun onUpdateClick(position: Int, item: Item)
        fun onDeleteClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener){
        listener = clickListener
    }

    class ItemDiffUtil : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemRecyclerViewHolder {
        return ItemRecyclerViewHolder(
            ItemRecyclerElementBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), listener
        )
    }

    override fun onBindViewHolder(holder: ItemRecyclerViewHolder, position: Int) {
        holder.bind()
    }

    inner class ItemRecyclerViewHolder(private val binding: ItemRecyclerElementBinding, private val clickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            with(binding) {
                val item = currentList[adapterPosition]
                tvTitle.text = item.title
                btnUpdate.setOnClickListener {
                    clickListener.onUpdateClick(adapterPosition, item)
                }
                btnDelete.setOnClickListener {
                    clickListener.onDeleteClick(adapterPosition)
                }
            }
        }
    }
}