package com.kmno.tmdb.utils

/**
 * Created by Kamran Nourinezhad on 18 June-6 2025.
 * Copyright (c)  2025 MCI.
 */
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
