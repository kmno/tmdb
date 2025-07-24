package com.kmno.tmdb.presentation.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.kmno.tmdb.domain.movie.Movie
import com.kmno.tmdb.utils.ConnectivityObserver
import com.kmno.tmdb.utils.UiState
import com.kmno.tmdb.utils.ui.UiEvent
import com.kmno.tmdb.utils.ui.shared.components.SharedSnackbarHost
import com.kmno.tmdb.utils.ui.shared.showSnackbar

/**
 * Created by Kamran Nourinezhad on 16 June-6 2025.
 * Copyright (c)  2025 MCI.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    movieId: Int,
    navController: NavController,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val networkStatus by viewModel.isNetworkAvailable.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    val movieDetailsState by viewModel.movieDetails.collectAsStateWithLifecycle()
    val watchlistState by viewModel.watchlist.collectAsStateWithLifecycle()
    val watchlistLoading by viewModel.isWatchlistProcessing.collectAsStateWithLifecycle()

    val movie = (movieDetailsState as? UiState.Success)?.data

    /**
     * A derived state that determines whether the current movie is in the watchlist.
     *
     * This state is recomputed whenever the `movie` or `watchlistState` changes.
     * It checks if the `movie` exists and if its ID matches any movie ID in the `watchlistState`.
     *
     * @property isInWatchlist A boolean value indicating if the movie is in the watchlist.
     */
    val isInWatchlist by rememberSaveable(movie, watchlistState) {
        derivedStateOf {
            movie?.let { m -> watchlistState.any { it.id == m.id } } ?: false
        }
    }

    // Load movie details when the screen is launched
    LaunchedEffect(movieId) {
        viewModel.loadMovieDetails(movieId)

        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    showSnackbar(
                        snackbarHostState,
                        message = event.message,
                        actionLabel = event.actionLabel,
                        onAction = event.onAction
                    )
                }

                else -> Unit
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movie Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SharedSnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            //check network status
            if (networkStatus is ConnectivityObserver.Status.Unavailable) {
                Text(
                    "No internet connection",
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Red)
                        .padding(8.dp)
                )
            }

            when (movieDetailsState) {
                is UiState.Loading -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                is UiState.Error -> Column(Modifier.fillMaxSize(), Arrangement.Center) {
                    Text((movieDetailsState as UiState.Error).message)
                    Button(onClick = { viewModel.loadMovieDetails(movieId) }) {
                        Text("Retry")
                    }
                }

                is UiState.Success -> {
                    movie?.let {
                        MovieDetailsContent(movie)
                        WatchlistButton(
                            isInWatchlist = isInWatchlist,
                            watchlistLoading = watchlistLoading,
                            onToggle = {
                                if (isInWatchlist) viewModel.removeFromWatchlist(it)
                                else viewModel.addToWatchlist(it)
                            }
                        )

                    }
                }
            }
        }
    }
}

@Composable
fun MovieDetailsContent(
    movie: Movie,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = movie.title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        movie.posterPath?.let {
            val imageUrl = "https://image.tmdb.org/t/p/w500$it"
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = movie.title,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("Release: ${movie.releaseDate ?: "N/A"}")
        Spacer(modifier = Modifier.height(8.dp))
        Text(movie.overview)
    }
}

@Composable
fun WatchlistButton(
    isInWatchlist: Boolean,
    onToggle: () -> Unit,
    watchlistLoading: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onToggle,
            enabled = !watchlistLoading,
        ) {
            if (watchlistLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Processing...")
            } else {
                Text(if (isInWatchlist) "Remove from Watchlist" else "Add to Watchlist")
            }
        }
    }
}