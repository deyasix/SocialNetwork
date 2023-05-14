package com.example.myprofilemarkup.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myprofilemarkup.databinding.ActivityMainBinding
import com.example.myprofilemarkup.utilits.Constants


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textViewNameSurname.text = this.intent.getStringExtra(Constants.NAME_SURNAME)
    }
}