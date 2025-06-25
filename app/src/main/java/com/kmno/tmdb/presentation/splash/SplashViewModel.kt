package com.kmno.tmdb.presentation.splash

import androidx.lifecycle.ViewModel
import com.kmno.tmdb.utils.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

/**
 * Created by Kamran Nourinezhad on 24 June-6 2025.
 * Copyright (c)  2025 MCI.
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    userPreferences: UserPreferences
) : ViewModel() {
    val userPrefs = userPreferences
}