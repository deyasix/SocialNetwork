package com.example.myprofilemarkup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.myprofilemarkup.databinding.ActivityRegistrationBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {

    companion object {
        const val PREFERENCE_NAME = "MyProfileMarkupPreference"
    }

    private lateinit var binding: ActivityRegistrationBinding

    private val dataStore by preferencesDataStore(PREFERENCE_NAME)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var email: String? = null
        lifecycleScope.launch {
            try {
                email = dataStore.data.first()[stringPreferencesKey("email")]
            } catch (ex: Exception) {
                Log.e(ex.message, ex.toString())
            }
        }
        if (email != null) {
            navigateToMain(email.toString())
        }
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        restoreState(savedInstanceState)
        setContentView(binding.root)
        setupListeners()
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            binding.textInputEditTextEmail.setText(savedInstanceState.getString("EMAIL"))
            if (binding.textInputEditTextEmail.text?.isNotEmpty() == true) validateEmail()
            binding.textInputEditTextPassword.setText(savedInstanceState.getString("PASSWORD"))
            if (binding.textInputEditTextPassword.text?.isNotEmpty() == true) validatePassword()
            binding.checkBoxRemember.isChecked = savedInstanceState.getBoolean("REMEMBER")
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("EMAIL", binding.textInputEditTextEmail.text.toString())
        outState.putString("PASSWORD", binding.textInputEditTextPassword.text.toString())
        outState.putBoolean("REMEMBER", binding.checkBoxRemember.isChecked)
    }

    private fun registrationButtonClickEvent() {
        if (isValidate()) {
            val email = binding.textInputEditTextEmail.text.toString()
            if (binding.checkBoxRemember.isChecked) {
                lifecycleScope.launch {
                    dataStore.edit { settings ->
                        settings[stringPreferencesKey("email")] = email}
                }
            }
            navigateToMain(email)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
        } else {
            Toast.makeText(this, resources.getString(R.string.incorrect_password_email), Toast.LENGTH_LONG).show()
        }
    }

    private fun getNameSurname(email: String): String {
        val splitEmail = email.split("@")[0]
        return if (splitEmail.contains(".")) {
            val nameSurname = splitEmail.split(".")
            val (name, surname) = Pair(nameSurname[0], nameSurname[1])
            name.replaceFirstChar { it.uppercase() } + " " + surname.replaceFirstChar { it.uppercase() }
        } else splitEmail.replaceFirstChar { it.uppercase() }
    }

    private fun isValidate(): Boolean {
        val isValidEmail = validateEmail()
        val isValidPassword = validatePassword()
        return isValidEmail && isValidPassword
    }

    private fun setupListeners() {
        binding.textInputEditTextEmail.addTextChangedListener(TextFieldValidation(binding.textInputEditTextEmail))
        binding.textInputEditTextPassword.addTextChangedListener(TextFieldValidation(binding.textInputEditTextPassword))
        binding.registrationButton.setOnClickListener { registrationButtonClickEvent() }
    }

    private fun navigateToMain(email: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("nameSurname", getNameSurname(email))
        startActivity(intent)
    }

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {/* no action */}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {/* no action */}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            when (view) {
                binding.textInputEditTextEmail -> {
                    validateEmail()
                }

                binding.textInputEditTextPassword -> {
                    validatePassword()
                }
            }

        }
    }

    private fun validateEmail(): Boolean {
        val email = binding.textInputEditTextEmail.text.toString()
        val isEmptyEmail = email.trim().isEmpty()
        return if (!isEmptyEmail && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.textInputLayoutEmail.isErrorEnabled = false
            true
        } else {
            binding.textInputLayoutEmail.error = when {
                isEmptyEmail -> resources.getString(R.string.required_field)
                else -> resources.getString(R.string.invalid_email)
            }
            if (!isEmptyEmail) binding.textInputEditTextEmail.requestFocus()
            false
        }
    }

    private fun validatePassword(): Boolean {
        val password = binding.textInputEditTextPassword.text.toString()
        val isEmptyPassword = password.trim().isEmpty()
        return if (!isEmptyPassword && password.length >= 8 && password.any(Char::isDigit)
            && password.any(Char::isLowerCase) && password.any(Char::isUpperCase)) {
            binding.textInputLayoutPassword.isErrorEnabled = false
            true
        } else {
            binding.textInputLayoutPassword.error = when {
                isEmptyPassword -> resources.getString(R.string.required_field)
                password.length < 8 -> resources.getString(R.string.password_size)
                password.none(Char::isDigit) -> resources.getString(R.string.required_digit_password)
                else -> resources.getString(R.string.password_rules)
            }
            if (!isEmptyPassword) binding.textInputEditTextPassword.requestFocus()
            false
        }
    }
}