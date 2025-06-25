package com.kmno.tmdb.presentation.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmno.tmdb.domain.movie.Movie
import com.kmno.tmdb.domain.movie.MovieRepository
import com.kmno.tmdb.utils.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    /**
     * A `StateFlow` that represents the user's watchlist.
     *
     * This flow is initialized with an empty list and observes the watchlist data
     * from the repository. It uses the `stateIn` operator to ensure the flow is
     * lifecycle-aware and provides a default value.
     *
     * - `viewModelScope`: Ensures the flow is tied to the lifecycle of the ViewModel.
     * - `SharingStarted.Lazily`: Starts the flow only when it is collected.
     * - `emptyList()`: Provides an initial empty list as the default value.
     *
     * @property watchlist A `StateFlow` containing the list of movies in the watchlist.
     */
    val watchlist: StateFlow<List<Movie>> = repository.getWatchlist()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun removeFromWatchlist(movie: Movie) {
        viewModelScope.launch {
            _uiEvent.emit(
                UiEvent.ConfirmDialog(
                    title = "Remove from Watchlist",
                    message = "Are you sure you want to remove ${movie.title} from your watchlist?",
                    onConfirm = {
                        viewModelScope.launch {
                            try {
                                repository.removeFromWatchlist(movie)
                                _uiEvent.emit(UiEvent.ShowSnackbar("Removed ${movie.title} from watchlist"))
                            } catch (e: Exception) {
                                _uiEvent.emit(UiEvent.ShowSnackbar("Failed to remove ${movie.title} from watchlist"))
                            }
                        }
                    },
                    onDismiss = {}
                ))
        }
    }
}