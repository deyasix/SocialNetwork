package com.example.myprofilemarkup.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofilemarkup.R
import com.example.myprofilemarkup.databinding.UserItemBinding
import com.example.myprofilemarkup.utilits.Constants.SNACKBAR_DURATION
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
                    deleteItem(absoluteAdapterPosition, root)
                }
            }
        }
    }

    private fun removeAt(index: Int) {
        dataSet.removeAt(index)
        notifyItemRemoved(index)
    }

    fun deleteItem(index: Int, view: View) {
        val user = dataSet[index]
        removeAt(index)
        val context = view.context
        Snackbar.make(view, context.getString(R.string.removing_contact_success), SNACKBAR_DURATION)
            .setAction(context.getString(R.string.removing_contact_cancel)) {
                cancelDeletingItem(user, index, view.context)
            }.show()
    }

    private fun cancelDeletingItem(user: User, index: Int, context: Context) {
        addAt(index, user)
        Toast.makeText(
            context, context.getString(R.string.canceled_removing_contact), Toast.LENGTH_SHORT
        ).show()
    }

    private fun addAt(index: Int, user: User) {
        dataSet.add(index, user)
        notifyItemInserted(index)
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