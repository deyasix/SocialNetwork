package com.example.myprofilemarkup.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myprofilemarkup.R
import com.example.myprofilemarkup.databinding.ActivityMainBinding
import com.example.myprofilemarkup.utilits.Constants


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.textViewNameSurname.text = intent.getStringExtra(Constants.NAME_SURNAME)
        setViewContactClickListener()
    }

    private fun setViewContactClickListener() {
        binding.buttonViewMyContacts.setOnClickListener {
            startActivity(Intent(this, ContactsActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
        }
    }
}