package com.kmno.tmdb.presentation.upcoming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmno.tmdb.domain.Movie
import com.kmno.tmdb.domain.MovieRepository
import com.kmno.tmdb.utils.ConnectivityObserver
import com.kmno.tmdb.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Kamran Nourinezhad on 15 June-6 2025.
 * Copyright (c)  2025 MCI.
 */

@HiltViewModel
class UpcomingViewModel @Inject constructor(
    private val repository: MovieRepository,
    connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val network = connectivityObserver.observe()
    val isNetworkAvailable = network.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        ConnectivityObserver.Status.Available
    )

    private val _movies = MutableStateFlow<UiState<List<Movie>>>(UiState.Loading)
    val movies: StateFlow<UiState<List<Movie>>> = _movies

    init {
        loadNowPlaying()
    }

    fun loadNowPlaying() {
        viewModelScope.launch {
            _movies.value = UiState.Loading
            delay(3000)
            try {
                val movies = withContext(Dispatchers.IO) { repository.getNowPlayingMovies() }
                _movies.value = UiState.Success(movies)
            } catch (e: IOException) {
                _movies.value = UiState.Error("No internet connection")
            } catch (e: HttpException) {
                _movies.value = UiState.Error("Server error: ${e.code()}")
            } catch (e: Exception) {
                _movies.value = UiState.Error("Unknown error")
            }
        }
    }
}
