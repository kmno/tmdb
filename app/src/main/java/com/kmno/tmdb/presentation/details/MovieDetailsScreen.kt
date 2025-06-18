package com.kmno.tmdb.presentation.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.kmno.tmdb.domain.Movie

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
    val movieDetailsState by viewModel.movieDetails.collectAsStateWithLifecycle()
    val watchlistState by viewModel.watchlist.collectAsState()

    val isInWatchlistState = remember(movieDetailsState, watchlistState) {
        movieDetailsState?.let { movie -> watchlistState.any { it.id == movie.id } } ?: false
    }
    //val isInWatchlistState by viewModel.isInWatchlist(movieId).collectAsStateWithLifecycle(false)

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
        if (movieDetailsState == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            MovieDetailsContent(
                movie = movieDetailsState!!,
                isInWatchlist = isInWatchlistState,
                onWatchlistToggle = {
                    if (isInWatchlistState) viewModel.removeFromWatchlist(movieDetailsState!!)
                    else viewModel.addToWatchlist(movieDetailsState!!)
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