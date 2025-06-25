package com.kmno.tmdb.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmno.tmdb.domain.auth.AuthRepository
import com.kmno.tmdb.utils.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
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

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun login(onSuccess: () -> Unit) {
        if (!email.contains("@") || password.length < 6) {
            errorMessage = "Invalid email or password"
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            val result = repository.login(email, password)
            isLoading = false

            if (result.isSuccess) {
                userPrefs.saveToken("kamran_token") // Simulated token
                onSuccess()
            } else {
                errorMessage = result.exceptionOrNull()?.message ?: "Login failed"
            }
        }

    }
}