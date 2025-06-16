package com.kmno.tmdb.presentation.upcoming

import android.graphics.Movie
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by Kamran Nourinezhad on 15 June-6 2025.
 * Copyright (c)  2025 MCI.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpcomingScreen(
    viewModel: DummyUpcomingViewModel,
    onNavigateToSearch: () -> Unit,
    onNavigateToWatchlist: () -> Unit
) {
    // Implement the UI for the Upcoming Screen here
    // Use viewModel to get data and handle events
    // Call onNavigateToSearch and onNavigateToWatchlist when needed

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
            //items(movies) { movie ->
            // MovieItem(movie)
            // }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UpcomingScreenPreview() {
    UpcomingScreen(
        DummyUpcomingViewModel(),
        onNavigateToSearch = {},
        onNavigateToWatchlist = {}
    )
}

// Dummy ViewModel for Preview
class DummyUpcomingViewModel : ViewModel() {
    val movies: StateFlow<List<Movie>> = MutableStateFlow(
        listOf(
            Movie("Movie 1", "2025-06-15", "Overview 1", "/path1.jpg"),
            Movie("Movie 2", "2025-06-16", "Overview 2", "/path2.jpg")
        )
    )
}


//-------------------------------------------------------------

/*
@Composable
fun MovieItem(movie: Movie) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { */
/* TODO: Show detail or add action *//*
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
}*/
