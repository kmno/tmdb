package com.kmno.tmdb.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmno.tmdb.domain.Movie
import com.kmno.tmdb.domain.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Kamran Nourinezhad on 16 June-6 2025.
 * Copyright (c)  2025 MCI.
 */

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movieDetails = MutableStateFlow<Movie?>(null)
    val movieDetails: StateFlow<Movie?> = _movieDetails

    fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch {
            repository.fetchMovieDetails(movieId).collect {
                _movieDetails.value = it
            }
        }
    }

    val watchlist: StateFlow<List<Movie>> = repository.getWatchlist()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            emptyList()
        )

    /*fun isInWatchlist(movieId: Int): Flow<Boolean> =
        watchlist.map { list -> list.any { it.id == movieId } }*/

    fun addToWatchlist(movie: Movie) {
        viewModelScope.launch {
            Timber.d("Adding movie to watchlist: ${movie.id}")
            repository.addToWatchlist(movie)
        }
    }

    fun removeFromWatchlist(movie: Movie) {
        viewModelScope.launch {
            repository.removeFromWatchlist(movie)
        }
    }
}