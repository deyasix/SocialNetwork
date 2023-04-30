package com.example.myprofilemarkup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myprofilemarkup.databinding.ActivityRegistrationBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreference =  getSharedPreferences(getString(R.string.preference_file_key),
            Context.MODE_PRIVATE)
        val email = sharedPreference.getString("E-mail", null)
        if (email != null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("nameSurname", getNameSurname(email))
            startActivity(intent)
        } else {
            binding = ActivityRegistrationBinding.inflate(layoutInflater)
            setContentView(binding.root)
            setupListeners()
            binding.registrationButton.setOnClickListener(this::registrationButtonClickEvent)
        }
    }

    private fun registrationButtonClickEvent(view: View) {
        if (isValidate()) {
            val sharedPreference =  getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE)
            if (binding.checkBoxRemember.isChecked) {
                with (sharedPreference.edit()) {
                    putString("E-mail", binding.textInputEditTextEmail.text.toString())
                    apply()
                }
            }
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("nameSurname", getNameSurname(binding.textInputEditTextEmail.text.toString()))
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        } else {
            Toast.makeText(this, "Incorrect e-mail or password!", Toast.LENGTH_LONG).show()
        }
    }

    private fun getNameSurname(email: String): String {
        val splitEmail = email.split("@")[0]
        return if (splitEmail.contains(".")) {
            val nameSurname = splitEmail.split(".")
            val name = nameSurname[0]
            val surname = nameSurname[1]
            name.replaceFirstChar { it.uppercase() } + " " + surname.replaceFirstChar { it.uppercase() }
        } else splitEmail.replaceFirstChar { it.uppercase() }
    }

    private fun isValidate(): Boolean = validateEmail() && validatePassword()

    private fun setupListeners() {
        binding.textInputEditTextEmail.addTextChangedListener(TextFieldValidation(binding.textInputEditTextEmail))
        binding.textInputEditTextPassword.addTextChangedListener(TextFieldValidation(binding.textInputEditTextPassword))
    }

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            when (view.id) {
                R.id.textInputEditTextEmail -> {
                    validateEmail()
                }

                R.id.textInputEditTextPassword -> {
                    validatePassword()
                }
            }
        }
    }

    private fun validateEmail(): Boolean {
        if (binding.textInputEditTextEmail.text.toString().trim().isEmpty()) {
            binding.textInputLayoutEmail.error = "Required Field!"
            binding.textInputEditTextEmail.requestFocus()
            return false
        } else if (!isValidEmail(binding.textInputEditTextEmail.text.toString())) {
            binding.textInputLayoutEmail.error = "Invalid Email!"
            binding.textInputEditTextEmail.requestFocus()
            return false
        } else {
            binding.textInputLayoutEmail.isErrorEnabled = false
        }
        return true
    }

    private fun validatePassword(): Boolean {
        if (binding.textInputEditTextPassword.text.toString().trim().isEmpty()) {
            binding.textInputLayoutPassword.error = "Required Field!"
            binding.textInputEditTextPassword.requestFocus()
            return false
        } else if (binding.textInputEditTextPassword.text.toString().length < 8) {
            binding.textInputLayoutPassword.error = "password can't be less than 8"
            binding.textInputEditTextPassword.requestFocus()
            return false
        } else if (!isStringContainNumber(binding.textInputEditTextPassword.text.toString())) {
            binding.textInputLayoutPassword.error = "Required at least 1 digit"
            binding.textInputEditTextPassword.requestFocus()
            return false
        } else if (!isStringLowerAndUpperCase(binding.textInputEditTextPassword.text.toString())) {
            binding.textInputLayoutPassword.error =
                "Password must contain upper and lower case letters"
            binding.textInputEditTextPassword.requestFocus()
            return false
        } else {
            binding.textInputLayoutPassword.isErrorEnabled = false
        }
        return true
    }

    private fun isValidEmail(target: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    private fun isStringLowerAndUpperCase(target: CharSequence): Boolean {
        return target.any(Char::isLowerCase) && target.any(Char::isUpperCase)
    }

    private fun isStringContainNumber(target: CharSequence): Boolean {
        return target.any(Char::isDigit)
    }
}