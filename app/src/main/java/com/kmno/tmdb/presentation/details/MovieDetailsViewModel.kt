package com.kmno.tmdb.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmno.tmdb.domain.Movie
import com.kmno.tmdb.domain.MovieRepository
import com.kmno.tmdb.utils.ConnectivityObserver
import com.kmno.tmdb.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Kamran Nourinezhad on 16 June-6 2025.
 * Copyright (c)  2025 MCI.
 */

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: MovieRepository,
    connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val network = connectivityObserver.observe()
    val isNetworkAvailable = network.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        ConnectivityObserver.Status.Available
    )

    private val _movieDetails = MutableStateFlow<UiState<Movie?>>(UiState.Loading)
    val movieDetails: StateFlow<UiState<Movie?>> = _movieDetails

    /*fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch {
            repository.fetchMovieDetails(movieId).collect {
                _movieDetails.value = it
            }
        }
    }*/

    fun loadMovieDetails(movieId: Int) = viewModelScope.launch {
        _movieDetails.value = UiState.Loading
        try {
            val movie = repository.fetchMovieDetails(movieId).first()
            _movieDetails.value = UiState.Success(movie)
        } catch (e: IOException) {
            _movieDetails.value = UiState.Error("No internet connection")
        } catch (e: HttpException) {
            _movieDetails.value = UiState.Error("Server error: ${e.code()}")
        } catch (e: Exception) {
            _movieDetails.value = UiState.Error("Unknown error")
        }
    }

    val watchlist: StateFlow<List<Movie>> = repository.getWatchlist()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            emptyList()
        )

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