package com.example.tp2.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.tp2.model.User

class UserAdapter(val clickListener: UserListener) : ListAdapter<User, MyListAdapter.ViewHolder>(UserDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyListAdapter.ViewHolder {
        return MyListAdapter.ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyListAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }
}

class UserListener(val clickListener: (user: User) -> Unit) {
    fun onClick(user: User) = clickListener(user)
}