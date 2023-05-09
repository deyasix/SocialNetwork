package com.example.myprofilemarkup.ui

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.myprofilemarkup.R
import com.example.myprofilemarkup.databinding.ActivityRegistrationBinding
import com.example.myprofilemarkup.utilits.getDataValue
import com.example.myprofilemarkup.utilits.saveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val EMAIL = "EMAIL"
private const val PASSWORD = "PASSWORD"
private const val REMEMBER = "REMEMBER"
private const val AT_SIGN = '@'
private const val WHITESPACE_SYMBOL = ' '
private const val DOT_SYMBOL = '.'

class AuthActivity : AppCompatActivity() {

    private val binding: ActivityRegistrationBinding by lazy {
        ActivityRegistrationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAutoLogin()
        setupListeners()
        setContentView(binding.root)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?, persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        if (savedInstanceState != null) {
            binding.textInputEditTextEmail.setText(savedInstanceState.getString(EMAIL))
            binding.textInputEditTextPassword.setText(savedInstanceState.getString(PASSWORD))
            binding.checkBoxRemember.isChecked = savedInstanceState.getBoolean(REMEMBER)
        }
    }

    /**
     * Method that checks if user is log in or not. If user log in before, navigate to main screen.
     */
    private fun checkAutoLogin() {
        lifecycleScope.launch {
            val email = getDataValue(EMAIL)
            val password = getDataValue(PASSWORD)
            if (email.isNotEmpty() && password.isNotEmpty()) navigateToMain(email)
        }
    }

    /**
     * Overridden method for saving state of text fields and checkbox.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EMAIL, binding.textInputEditTextEmail.text.toString())
        outState.putString(PASSWORD, binding.textInputEditTextPassword.text.toString())
        outState.putBoolean(REMEMBER, binding.checkBoxRemember.isChecked)
    }

    /**
     * Method that represents event of click on registration button. If user wants to remember him
     * in the system, add to dataStore user's email.
     */
    private fun registrationButtonClickEvent() {
        if (isValidate()) {
            val email = binding.textInputEditTextEmail.text.toString()
            val password = binding.textInputEditTextPassword.text.toString()
            if (binding.checkBoxRemember.isChecked) {
                lifecycleScope.launch(Dispatchers.IO) {
                    saveData(EMAIL, email)
                    saveData(PASSWORD, password)
                }
            }
            navigateToMain(email)
        } else {
            Toast.makeText(
                this,
                resources.getString(R.string.incorrect_password_email), Toast.LENGTH_LONG
            ).show()
        }
    }

    /**
     * Method that gets name and surname from email (parsing).
     */
    private fun getNameSurname(email: String): String {
        val splitEmail = email.split(AT_SIGN)[0]
        return if (splitEmail.contains(DOT_SYMBOL)) {
            val nameSurname = splitEmail.split(DOT_SYMBOL)
            val (name, surname) = Pair(nameSurname[0], nameSurname[1])
            name.replaceFirstChar { it.uppercase() } + WHITESPACE_SYMBOL +
                    surname.replaceFirstChar { it.uppercase() }
        } else splitEmail.replaceFirstChar { it.uppercase() }
    }

    /**
     * Method that checks validating of text fields. Each validation should be performed, so
     * before checks if all validation is satisfied, we add two variables.
     */
    private fun isValidate(): Boolean {
        val isValidEmail = validateEmail()
        val isValidPassword = validatePassword()
        return isValidEmail && isValidPassword
    }

    private fun setupListeners() {
        setListenerToEmailEditText()
        setListenerToPasswordEditText()
        setListenerToRegisterButton()
    }

    private fun setListenerToRegisterButton() {
        binding.registrationButton.setOnClickListener { registrationButtonClickEvent() }
    }

    private fun setListenerToPasswordEditText() {
        binding.textInputEditTextPassword.doOnTextChanged { _, _, _, _ -> validatePassword() }
    }

    private fun setListenerToEmailEditText() {
        binding.textInputEditTextEmail.doOnTextChanged { _, _, _, _ -> validateEmail() }
    }

    /**
     * Method that navigates to the main screen using animation.
     */
    private fun navigateToMain(email: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("nameSurname", getNameSurname(email))
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
        finish()
    }

    /**
     * Method for validating email. Email should be not empty and satisfy EMAIL_ADDRESS pattern.
     */
    private fun validateEmail(): Boolean {
        val email = binding.textInputEditTextEmail.text.toString()
        val isEmptyEmail = email.trim().isEmpty()
        return if (!isEmptyEmail && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.textInputLayoutEmail.isErrorEnabled = false
            true
        } else {
            showEmailError(isEmptyEmail)
            false
        }
    }

    /**
     * Method for validating password. Password should be not empty, should have at least 1 digit,
     * 1 upper case letter and 1 lower case letter.
     */
    private fun validatePassword(): Boolean {
        val password = binding.textInputEditTextPassword.text.toString()
        return if (isPasswordCorrect(password)) {
            binding.textInputLayoutPassword.isErrorEnabled = false
            true
        } else {
            showPasswordError(password)
            false
        }
    }

    private fun isPasswordCorrect(password: String): Boolean {
        return (password.trim().isNotEmpty() && password.length >= 8 && password.any(Char::isDigit)
                && password.any(Char::isLowerCase) && password.any(Char::isUpperCase))
    }

    private fun showPasswordError(password: String) {
        val isEmptyPassword = password.trim().isEmpty()
        binding.textInputLayoutPassword.error = when {
            isEmptyPassword -> resources.getString(R.string.required_field)
            password.length < 8 -> resources.getString(R.string.password_size)
            password.none(Char::isDigit) -> resources.getString(R.string.required_digit_password)
            else -> resources.getString(R.string.password_rules)
        }
        if (!isEmptyPassword) binding.textInputEditTextPassword.requestFocus()
    }

    private fun showEmailError(isEmptyEmail: Boolean) {
        binding.textInputLayoutEmail.error = when {
            isEmptyEmail -> resources.getString(R.string.required_field)
            else -> resources.getString(R.string.invalid_email)
        }
        if (!isEmptyEmail) binding.textInputEditTextEmail.requestFocus()
    }
}