package com.kmno.tmdb.presentation.upcoming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmno.tmdb.domain.Movie
import com.kmno.tmdb.domain.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Kamran Nourinezhad on 15 June-6 2025.
 * Copyright (c)  2025 MCI.
 */

@HiltViewModel
class UpcomingViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    init {
        loadNowPlaying()
    }

    private fun loadNowPlaying() {
        viewModelScope.launch {
            val movies = repository.getNowPlayingMovies()
            _movies.value = movies
        }
    }
}
