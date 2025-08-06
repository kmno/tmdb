package com.kmno.tmdb.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmno.tmdb.domain.auth.AuthRepository
import com.kmno.tmdb.utils.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Kamran Nourinezhad on 24 June-6 2025.
 * Copyright (c)  2025 MCI.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val userPrefs: UserPreferences
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
    }

    fun login(onSuccess: () -> Unit) {
        if (!_email.value.contains("@") || _password.value.length < 6) {
            _errorMessage.value = "Invalid email or password"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            delay(2000)
            val result = repository.login(_email.value, _password.value)
            _isLoading.value = false

            if (result.isSuccess) {
                userPrefs.saveToken("kamran_token") // Simulated token
                onSuccess()
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Login failed"
            }
        }

    }
}