package com.example.myprofilemarkup.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.myprofilemarkup.data.User
import com.example.myprofilemarkup.databinding.FragmentAddContactBinding

class AddContactDialogFragment(val action: (user: User) -> Unit) : DialogFragment() {


    private var _binding: FragmentAddContactBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddContactBinding.inflate(inflater, container, false)
        binding.buttonSaveContact.setOnClickListener{
            val career = binding.textInputEditTextContactCareer.text.toString()
            val name = binding.textInputEditTextContactUsername.text.toString()
            action(User(5, "", name, career, ""))
            dismiss()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "AddContactDialog"
    }

}