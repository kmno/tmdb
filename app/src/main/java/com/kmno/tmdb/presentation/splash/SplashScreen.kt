package com.kmno.tmdb.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kmno.tmdb.utils.UserPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull


/**
 * Created by Kamran Nourinezhad on 24 June-6 2025.
 * Copyright (c)  2025 MCI.
 */

@Composable
fun SplashScreen(
    userPrefs: UserPreferences,
    onNavigateToLogin: () -> Unit,
    onNavigateToUpcoming: () -> Unit
) {

    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        userPrefs.authToken.firstOrNull().let { token ->
            delay(3000)
            if (token != null) {
                isLoading = false
                onNavigateToUpcoming()
            } else {
                isLoading = false
                onNavigateToLogin()
            }
        }
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}