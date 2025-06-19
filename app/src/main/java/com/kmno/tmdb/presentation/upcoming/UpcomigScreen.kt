package com.kmno.tmdb.presentation.upcoming

import Constants
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.kmno.tmdb.domain.Movie
import toReadableDate

/**
 * Created by Kamran Nourinezhad on 15 June-6 2025.
 * Copyright (c)  2025 MCI.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpcomingScreen(
    viewModel: UpcomingViewModel,
    nav: NavController,
    onNavigateToSearch: () -> Unit,
    onNavigateToWatchlist: () -> Unit
) {

    val movies by viewModel.movies.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Now Playing in Toronto") },
                actions = {
                    TextButton(onClick = onNavigateToSearch) { Text("Search") }
                    TextButton(onClick = onNavigateToWatchlist) { Text("Watchlist") }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(movies) { movie ->
                MovieItem(movie, nav)
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, nav: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                nav.navigate("details/${movie.id}")
            },
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val imageUrl = movie.posterPath?.let { Constants.IMAGE_BASE_URL + it }
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = movie.title,
            modifier = Modifier.size(100.dp, 150.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(text = movie.title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Text(text = movie.releaseDate?.toReadableDate() ?: "Unknown date")
            Spacer(Modifier.height(8.dp))
            Text(text = movie.overview, maxLines = 3, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

