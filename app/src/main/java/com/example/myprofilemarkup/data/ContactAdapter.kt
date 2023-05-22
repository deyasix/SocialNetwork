package com.example.myprofilemarkup.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofilemarkup.databinding.UserItemBinding

class ContactAdapter(private val dataSet: Array<User>) : RecyclerView.Adapter<ContactAdapter.UserViewHolder>() {

    inner class UserViewHolder(contactBinding: UserItemBinding) : RecyclerView.ViewHolder(contactBinding.root) {
        private val binding = contactBinding

        fun bind(user: User) {
            binding.textViewNameItem.text = user.name
            binding.textViewCareerItem.text = user.career
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            UserItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

}