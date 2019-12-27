package com.example.tp2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tp2.apiService.MarsProperty
import com.example.tp2.databinding.ApiItemViewBinding

class MarsAdapter(val clickListener: MarsListener) : ListAdapter<MarsProperty, MarsAdapter.ViewHolder>(MarsDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    class ViewHolder private constructor(val binding: ApiItemViewBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: MarsProperty, clickListener: MarsListener) {
            binding.mars = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ApiItemViewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class MarsListener(val clickListener: (mars: MarsProperty) -> Unit) {
    fun onClick(mars: MarsProperty) = clickListener(mars)
}

class MarsDiffCallback : DiffUtil.ItemCallback<MarsProperty>() {
    override fun areItemsTheSame(oldItem: MarsProperty, newItem: MarsProperty): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MarsProperty, newItem: MarsProperty): Boolean {
        return oldItem == newItem
    }
}