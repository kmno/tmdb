package com.kmno.tmdb.presentation.watchlist

import Constants
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.kmno.tmdb.domain.movie.Movie
import com.kmno.tmdb.utils.ui.UiEvent
import com.kmno.tmdb.utils.ui.shared.ConfirmationDialog
import com.kmno.tmdb.utils.ui.shared.components.SharedSnackbarHost
import com.kmno.tmdb.utils.ui.shared.showSnackbar
import toReadableDate

/**
 * Created by Kamran Nourinezhad on 16 June-6 2025.
 * Copyright (c)  2025 MCI.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(
    viewModel: WatchListViewModel,
    onBack: () -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }

    var confirmDialogData by remember { mutableStateOf<UiEvent.ConfirmDialog?>(null) }

    val watchlist by viewModel.watchlist.collectAsState()

    LaunchedEffect(Unit) {
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

                is UiEvent.ConfirmDialog -> {
                    confirmDialogData = event
                }

                is UiEvent.ShowDialog -> TODO()
                is UiEvent.Toast -> TODO()
            }
        }
    }

    confirmDialogData?.let { dialogData ->
        ConfirmationDialog(
            title = dialogData.title,
            message = dialogData.message,
            onConfirm = {
                dialogData.onConfirm?.invoke()
                confirmDialogData = null // ✅ dismiss dialog
            },
            onDismiss = {
                dialogData.onDismiss?.invoke()
                confirmDialogData = null // ✅ dismiss dialog
            },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Watchlist") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SharedSnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(watchlist) { movie ->
                WatchlistItem(movie, onRemove = { viewModel.removeFromWatchlist(movie) })
            }
        }
    }
}

@Composable
fun WatchlistItem(
    movie: Movie,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val imageUrl = movie.posterPath?.let { Constants.IMAGE_BASE_URL + it }
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = movie.title,
            modifier = Modifier.size(100.dp, 150.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(text = movie.title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Text(text = movie.releaseDate?.toReadableDate() ?: "Unknown date")
            Spacer(Modifier.height(8.dp))
            Text(text = movie.overview, maxLines = 3, style = MaterialTheme.typography.bodyMedium)
        }
        IconButton(onClick = {
            onRemove()
        }) {
            Icon(Icons.Default.Delete, contentDescription = "Remove from Watchlist")
        }
    }
}