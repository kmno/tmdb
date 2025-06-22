package com.kmno.tmdb.utils.ui.shared.components

/**
 * Created by Kamran Nourinezhad on 19 June-6 2025.
 * Copyright (c)  2025 MCI.
 */
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable

@Composable
fun SharedSnackbarHost(
    hostState: SnackbarHostState
) {
    SnackbarHost(hostState = hostState)
}