package com.example.myprofilemarkup.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofilemarkup.R
import com.example.myprofilemarkup.data.ContactAdapter
import com.example.myprofilemarkup.databinding.ActivityContactsBinding


class ContactsActivity : AppCompatActivity() {

    private val binding: ActivityContactsBinding by lazy {
        ActivityContactsBinding.inflate(layoutInflater)
    }

    private val viewModel : ContactViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setRecyclerView()
        binding.textViewAddContacts.setOnClickListener {
            AddContactDialogFragment(viewModel::addUser).show(supportFragmentManager, AddContactDialogFragment.TAG)
        }
    }

    private fun setRecyclerView() {
        val contactAdapter = ContactAdapter(viewModel.uiState.value)
        with (binding.recyclerViewContacts) {
            layoutManager = LinearLayoutManager(this@ContactsActivity)
            adapter = contactAdapter
        }
        val dividerItemDecoration = DividerItemDecoration(this, RecyclerView.VERTICAL)
        val divider = ContextCompat.getDrawable(this@ContactsActivity, R.drawable.contacts_divider)
        if (divider != null) {
            dividerItemDecoration.setDrawable(divider)
        }
        binding.recyclerViewContacts.addItemDecoration(dividerItemDecoration)
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(v: RecyclerView, h: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder) = false
            override fun onSwiped(h: RecyclerView.ViewHolder, dir: Int) = contactAdapter.deleteItem(h.absoluteAdapterPosition, h.itemView)
        }).attachToRecyclerView(binding.recyclerViewContacts)
    }



}