package com.kmno.tmdb.utils.ui

/**
 * Created by Kamran Nourinezhad on 19 June-6 2025.
 * Copyright (c)  2025 MCI.
 */
sealed class UiEvent {

    data class ShowSnackbar(
        val message: String,
        val actionLabel: String? = null,
        val onAction: (() -> Unit)? = null
    ) : UiEvent()

    data class ShowDialog(
        val title: String,
        val message: String,
        val confirmLabel: String = "OK",
        val onConfirm: (() -> Unit)? = null
    ) : UiEvent()

    data class ConfirmDialog(
        val title: String,
        val message: String,
        val confirmLabel: String = "Yes",
        val dismissLabel: String = "Cancel",
        val onConfirm: (() -> Unit)? = null,
        val onDismiss: (() -> Unit)? = null,
    ) : UiEvent()

    data class Toast(val message: String) : UiEvent()

}