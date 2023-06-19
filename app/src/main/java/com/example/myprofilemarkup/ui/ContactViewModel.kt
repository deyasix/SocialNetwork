package com.example.myprofilemarkup.ui

import androidx.lifecycle.ViewModel
import com.example.myprofilemarkup.data.User
import com.example.myprofilemarkup.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ContactViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(mutableListOf<User>())
    val uiState: StateFlow<MutableList<User>> = _uiState.asStateFlow()

    init {
        _uiState.update { currentState -> currentState.apply {addAll(UserRepository.users)} }
    }

    fun getUser(index: Int) : User {
        return _uiState.value[index]
    }

    fun addUser(user : User) {
        _uiState.update { currentState -> currentState.apply { add(user) } }
    }

    fun addAt(index: Int, user: User) {
        _uiState.update { currentState -> currentState.apply { add(index, user) } }
    }

    fun deleteAt(index: Int) {
        _uiState.update { currentState -> currentState.apply { deleteAt(index)} }
    }
}