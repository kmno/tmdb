package com.kmno.tmdb.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmno.tmdb.utils.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Created by Kamran Nourinezhad on 25 June-6 2025.
 * Copyright (c)  2025 MCI.
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userPref: UserPreferences
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn

    init {
        viewModelScope.launch {
            userPref.authToken.collect { token ->
                _isLoggedIn.value = !token.isNullOrEmpty()
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPref.clearToken()
            _isLoggedIn.value = false
        }
    }
}