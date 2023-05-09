package com.example.myprofilemarkup

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.myprofilemarkup.databinding.ActivityRegistrationBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val PREFERENCE_NAME = "MyProfileMarkupPreference"
private const val EMAIL = "EMAIL"

class AuthActivity : AppCompatActivity() {
//    companion object {
//        const val PREFERENCE_NAME = "MyProfileMarkupPreference"
//    }

    private lateinit var binding: ActivityRegistrationBinding
//    private val binding: ActivityRegistrationBinding by lazy {ActivityRegistrationBinding.inflate(layoutInflater)}

    private val dataStore by preferencesDataStore(PREFERENCE_NAME)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAutoLogin()
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setupListeners()
        setContentView(binding.root)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?, persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        if (savedInstanceState != null) {
            binding.textInputEditTextEmail.setText(savedInstanceState.getString(EMAIL))
            binding.textInputEditTextPassword.setText(savedInstanceState.getString("PASSWORD"))
            binding.checkBoxRemember.isChecked = savedInstanceState.getBoolean("REMEMBER")
        }
    }

    /**
     * Method that checks if user is log in or not. If user log in before, navigate to main screen.
     */
    private fun checkAutoLogin() {
        lifecycleScope.launch(Dispatchers.IO) {
//            try {
//            Log.d(TAG, "withContext thread: ${Thread.currentThread().name}")
            dataStore.data.map { preferences ->
                    preferences[stringPreferencesKey(EMAIL)] ?: "" // TODO extract all literals to constants
                }
                    .collect { if (it.isNotEmpty()) navigateToMain(it) }
//            } catch (ex: Throwable) {
//                Log.e(TAG, ex.toString())
//            }
        }
    }

    /**
     * Overridden method for saving state of text fields and checkbox.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EMAIL, binding.textInputEditTextEmail.text.toString())
        outState.putString("PASSWORD", binding.textInputEditTextPassword.text.toString())
        outState.putBoolean("REMEMBER", binding.checkBoxRemember.isChecked)
    }

    /**
     * Method that represents event of click on registration button. If user wants to remember him
     * in the system, add to dataStore user's email.
     */
    private fun registrationButtonClickEvent() {
        if (isValidate()) {
            val email = binding.textInputEditTextEmail.text.toString()
            if (binding.checkBoxRemember.isChecked) {
                lifecycleScope.launch {// TODO add storing password to datastore
                    try {
                        dataStore.edit { settings ->
                            settings[stringPreferencesKey(EMAIL)] = email
                        }
                    } catch (ex: Throwable) {
                        Log.e(ex.message, ex.toString())
                    }
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
        val splitEmail = email.split("@")[0]
        return if (splitEmail.contains(".")) {
            val nameSurname = splitEmail.split(".")
            val (name, surname) = Pair(nameSurname[0], nameSurname[1])
            name.replaceFirstChar { it.uppercase() } + ' ' + surname.replaceFirstChar { it.uppercase() }
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
//        binding.textInputEditTextPassword.addTextChangedListener(
//            TextFieldValidation(binding.textInputEditTextPassword)
//        )
        binding.textInputEditTextPassword.doOnTextChanged{ _,_,_,_ -> // TODO here was changed, need to change for email
            validatePassword()
        }
    }

    private fun setListenerToEmailEditText() {
        with(binding) { // ← scope function with
            textInputEditTextEmail.addTextChangedListener(
                TextFieldValidation(textInputEditTextEmail)
            )
        }
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
     * Class for text field validation in real time.
     */
    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {/* no action */
        }

        override fun beforeTextChanged(
            s: CharSequence?, start: Int, count: Int, after: Int
        ) {/* no action */
        }

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

    /**
     * Method for validating email. Email should be not empty and satisfy EMAIL_ADDRESS pattern.
     */
    private fun validateEmail(): Boolean {
        val email = binding.textInputEditTextEmail.text.toString()
        val isEmptyEmail = email.trim().isEmpty()
        return if (!isEmptyEmail && Patterns.EMAIL_ADDRESS.matcher(email).matches()) { // TODO maybe extract logic to separated functions
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

    /**
     * Method for validating password. Password should be not empty, should have at least 1 digit,
     * 1 upper case letter and 1 lower case letter.
     */
    private fun validatePassword(): Boolean {
        val password = binding.textInputEditTextPassword.text.toString()
        val isEmptyPassword = password.trim().isEmpty()
        return if (!isEmptyPassword && password.length >= 8 && password.any(Char::isDigit)
            && password.any(Char::isLowerCase) && password.any(Char::isUpperCase)
        ) {
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