package com.kmno.tmdb.utils.ui.shared

/**
 * Created by Kamran Nourinezhad on 19 June-6 2025.
 * Copyright (c)  2025 MCI.
 */

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult

suspend fun showSnackbar(
    snackbarHostState: SnackbarHostState,
    message: String,
    actionLabel: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Short,
    onAction: (() -> Unit)? = null
) {
    val result = snackbarHostState.showSnackbar(
        message = message,
        actionLabel = actionLabel,
        duration = duration
    )
    if (result == SnackbarResult.ActionPerformed) {
        onAction?.invoke()
    }
}