package com.example.myprofilemarkup.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myprofilemarkup.databinding.ActivityContactsBinding

class ContactsActivity : AppCompatActivity() {

    private val binding: ActivityContactsBinding by lazy {
        ActivityContactsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

}