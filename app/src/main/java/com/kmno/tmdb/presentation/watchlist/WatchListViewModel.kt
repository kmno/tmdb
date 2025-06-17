package com.kmno.tmdb.presentation.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmno.tmdb.domain.Movie
import com.kmno.tmdb.domain.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Kamran Nourinezhad on 16 June-6 2025.
 * Copyright (c)  2025 MCI.
 */
@HiltViewModel
class WatchListViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    val watchlist: StateFlow<List<Movie>> = repository.getWatchlist()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun removeFromWatchlist(movie: Movie) {
        viewModelScope.launch {
            repository.removeFromWatchlist(movie)
        }
    }
}