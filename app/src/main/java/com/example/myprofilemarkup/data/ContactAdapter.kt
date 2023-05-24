package com.example.myprofilemarkup.data

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofilemarkup.databinding.UserItemBinding
import com.example.myprofilemarkup.utilits.ext.loadPhoto
import com.google.android.material.snackbar.Snackbar


class ContactAdapter(private val dataSet: MutableList<User>) :
    RecyclerView.Adapter<ContactAdapter.UserViewHolder>() {

    inner class UserViewHolder(contactBinding: UserItemBinding) :
        RecyclerView.ViewHolder(contactBinding.root) {
        private val binding = contactBinding

        fun bind(user: User) {
            with(binding) {
                textViewNameItem.text = user.name
                textViewCareerItem.text = user.career
                imageViewPhotoItem.loadPhoto(user.photo)
                imageViewTrashBinItem.setOnClickListener {
                    deleteItem(user)
                }
            }
        }

        private fun deleteItem(user: User) {
            val index = absoluteAdapterPosition
            dataSet.remove(user)
            notifyItemRemoved(bindingAdapterPosition)
            notifyItemRangeChanged(bindingAdapterPosition, dataSet.size)
            Snackbar.make(binding.imageViewTrashBinItem, "Contact has been removed", 5000)
                .setAction("Cancel") {
                    cancelDeletingItem(user, index)
                }.show()
        }

        private fun cancelDeletingItem(user: User, index: Int) {
            dataSet.add(index, user)
            notifyItemInserted(index)
            notifyItemRangeChanged(bindingAdapterPosition, dataSet.size)
            Toast.makeText(
                binding.imageViewTrashBinItem.context,
                "Contact removing is canceled!",
                Toast.LENGTH_LONG
            ).show()
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