package com.kmno.tmdb.presentation.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.kmno.tmdb.domain.Movie
import com.kmno.tmdb.utils.ConnectivityObserver
import com.kmno.tmdb.utils.UiState
import timber.log.Timber

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

    val movieDetailsState by viewModel.movieDetails.collectAsStateWithLifecycle()
    val watchlistState by viewModel.watchlist.collectAsStateWithLifecycle()

    val isInWatchlistState = remember(movieDetailsState, watchlistState) {
        (movieDetailsState as? UiState.Success)?.data?.let { movie ->
            watchlistState.any { it.id == movie.id }
        } ?: false
    }

    // Load movie details when the screen is launched
    LaunchedEffect(movieId) {
        viewModel.loadMovieDetails(movieId)
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
        }
    ) { paddingValues ->

        Timber.d("Network status: $networkStatus")
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
            is UiState.Loading -> CircularProgressIndicator()
            is UiState.Error -> Column(Modifier.fillMaxSize(), Arrangement.Center) {
                Text((movieDetailsState as UiState.Error).message)
                Button(onClick = { viewModel.loadMovieDetails(movieId) }) {
                    Text("Retry")
                }
            }

            is UiState.Success -> MovieDetailsContent(
                movie = (movieDetailsState as UiState.Success).data!!,
                isInWatchlist = isInWatchlistState,
                onWatchlistToggle = {
                    if (isInWatchlistState) viewModel.removeFromWatchlist((movieDetailsState as UiState.Success).data!!)
                    else viewModel.addToWatchlist((movieDetailsState as UiState.Success).data!!)
                },
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            )
        }

    }
}

@Composable
fun MovieDetailsContent(
    movie: Movie,
    isInWatchlist: Boolean,
    onWatchlistToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("Release: ${movie.releaseDate ?: "N/A"}")
        Spacer(modifier = Modifier.height(8.dp))
        Text(movie.overview)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onWatchlistToggle,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(if (isInWatchlist) "Remove from Watchlist" else "Add to Watchlist")
        }
    }
}