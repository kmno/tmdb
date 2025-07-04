package com.kmno.tmdb.presentation.upcoming

import Constants
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.rememberAsyncImagePainter
import com.kmno.tmdb.domain.movie.Movie
import com.kmno.tmdb.utils.ConnectivityObserver
import kotlinx.coroutines.launch
import timber.log.Timber
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
    drawerState: DrawerState?
) {

    val networkStatus by viewModel.isNetworkAvailable.collectAsStateWithLifecycle()

    //val movies by viewModel.movies.collectAsStateWithLifecycle()
    val movies = viewModel.nowPlayingPagingFlow.collectAsLazyPagingItems()

    val scope = rememberCoroutineScope()

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {

        val lifecycleObserver = LifecycleEventObserver { _, event ->
            Timber.e("Lifecycle event: ${event.name}")
        }

        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Now Playing!") },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            drawerState?.open() // Assuming you have a drawer state to open the navigation drawer
                        }
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = null)
                    }
                }
                /*actions = {
                    TextButton(onClick = onNavigateToSearch) { Text("Search") }
                    TextButton(onClick = onNavigateToWatchlist) { Text("Watchlist") }
                }*/
            )
        }
    ) { padding ->

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

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(movies.itemCount) { index ->
                movies[index]?.let { movie ->
                    MovieItem(movie, nav)
                }
            }

            // Loading state when user scrolls
            if (movies.loadState.append is LoadState.Loading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            // Initial loading
            if (movies.loadState.refresh is LoadState.Loading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            // Error handling
            if (movies.loadState.refresh is LoadState.Error) {
                val error = movies.loadState.refresh as LoadState.Error
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Error: ${error.error.localizedMessage ?: "Unknown error"}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { movies.retry() }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }


        /* when (movies) {
             is UiState.Loading -> {
                 // Show a loading indicator
                 Box(
                     modifier = Modifier.fillMaxSize(), // The Box takes up all available space
                     contentAlignment = Alignment.Center
                 ) {
                     CircularProgressIndicator() // The indicator itself doesn't need fillMaxSize or wrapContentSize here
                 }// Content within the Box is
             }

             is UiState.Error -> Column(Modifier.fillMaxSize(), Arrangement.Center) {
                 Text((movies as UiState.Error).message)
                 Button(onClick = { viewModel.loadNowPlaying() }) {
                     Text("Retry")
                 }
             }

             is UiState.Success -> {
                 // Show the list of movies
                 // MovieList((movies as UiState.Success<List<Movie>>).data, padding, nav)
                 LazyColumn(
                     modifier = Modifier
                         .padding(padding)
                         .fillMaxSize(),
                     contentPadding = PaddingValues(8.dp)
                 ) {
                     items((movies as UiState.Success<List<Movie>>).data, key = { it.id }) { movie ->
                         MovieItem(movie, nav)
                     }
                 }
             }
         }
 */

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