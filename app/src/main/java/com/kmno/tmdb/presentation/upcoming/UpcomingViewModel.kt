package com.kmno.tmdb.presentation.upcoming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kmno.tmdb.domain.movie.MovieRepository
import com.kmno.tmdb.utils.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Created by Kamran Nourinezhad on 15 June-6 2025.
 * Copyright (c)  2025 MCI.
 */

@HiltViewModel
class UpcomingViewModel @Inject constructor(
    repository: MovieRepository,
    connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val network = connectivityObserver.observe()
    val isNetworkAvailable = network.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        ConnectivityObserver.Status.Available
    )

    // private val _movies = MutableStateFlow<UiState<List<Movie>>>(UiState.Loading)
    //val movies: StateFlow<UiState<List<Movie>>> = _movies

    val nowPlayingPagingFlow = repository
        .getNowPlayingPagingFlow()
        .cachedIn(viewModelScope)

    init {
        //  loadNowPlaying()
    }

    /* fun loadNowPlaying() {
         viewModelScope.launch {
             _movies.value = UiState.Loading
             delay(3000)
             try {
                 val movies = withContext(Dispatchers.IO) { repository.getNowPlayingMovies(page = 1) }
                 _movies.value = UiState.Success(movies)
             } catch (e: IOException) {
                 _movies.value = UiState.Error("No internet connection")
             } catch (e: HttpException) {
                 _movies.value = UiState.Error("Server error: ${e.code()}")
             } catch (e: Exception) {
                 _movies.value = UiState.Error("Unknown error")
             }
         }
     }*/
}
