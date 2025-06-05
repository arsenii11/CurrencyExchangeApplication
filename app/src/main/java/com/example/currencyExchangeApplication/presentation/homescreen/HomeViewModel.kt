package com.example.currencyExchangeApplication.presentation.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyExchangeApplication.data.database.entities.UserEntity
import com.example.currencyExchangeApplication.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel() {

    // StateFlow for storing current user
    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user.asStateFlow()

    init {
        // Load existing user when ViewModel is initialized
        viewModelScope.launch {
            _user.value = repository.fetchUser()
        }
    }

    /**
     * Saves user to database and updates state.
     */
    fun saveUser(nickname: String, email: String = "") {
        viewModelScope.launch {
            repository.saveUser(nickname, email)
            _user.value = repository.fetchUser() // Update user data
        }
    }

    /**
     * Deletes user from database and clears state.
     */
    fun deleteUser() {
        viewModelScope.launch {
            repository.deleteUser()
            _user.value = null // Clear user data
        }
    }
}