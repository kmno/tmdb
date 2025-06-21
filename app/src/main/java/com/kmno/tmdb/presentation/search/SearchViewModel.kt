package com.kmno.tmdb.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmno.tmdb.domain.Movie
import com.kmno.tmdb.domain.MovieRepository
import com.kmno.tmdb.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Kamran Nourinezhad on 16 June-6 2025.
 * Copyright (c)  2025 MCI.
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _uiState = MutableStateFlow<UiState<List<Movie>>>(UiState.Success(emptyList()))
    val uiState: StateFlow<UiState<List<Movie>>> = _uiState.asStateFlow()

    init {
        observeQuery()
    }

    @OptIn(FlowPreview::class)
    private fun observeQuery() {
        viewModelScope.launch {
            _query
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { newQuery ->
                    if (newQuery.isBlank()) {
                        _uiState.value = UiState.Success(emptyList())
                    } else {
                        searchMovies(newQuery)
                    }
                }
        }
    }

    private suspend fun searchMovies(query: String) {
        _uiState.value = UiState.Loading
        try {
            val results = repository.searchMovies(query)
            _uiState.value = UiState.Success(results)
        } catch (e: Exception) {
            _uiState.value = UiState.Error(e.localizedMessage ?: "Unexpected error")
        }
    }

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
    }
}