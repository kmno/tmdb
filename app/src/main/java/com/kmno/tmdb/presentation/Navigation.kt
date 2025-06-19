package com.kmno.tmdb.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kmno.tmdb.presentation.details.MovieDetailsScreen
import com.kmno.tmdb.presentation.search.SearchScreen
import com.kmno.tmdb.presentation.search.SearchViewModel
import com.kmno.tmdb.presentation.upcoming.UpcomingScreen
import com.kmno.tmdb.presentation.upcoming.UpcomingViewModel
import com.kmno.tmdb.presentation.watchlist.WatchListViewModel
import com.kmno.tmdb.presentation.watchlist.WatchlistScreen

/**
 * Created by Kamran Nourinezhad on 15 June-6 2025.
 * Copyright (c)  2025 MCI.
 */

object Routes {
    const val UPCOMING = "upcoming"
    const val SEARCH = "search"
    const val WATCHLIST = "watchlist"
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.UPCOMING // Replace with your actual start destination
    ) {
        composable(Routes.UPCOMING) {
            val vm = hiltViewModel<UpcomingViewModel>()
            UpcomingScreen(
                vm,
                navController,
                onNavigateToSearch = {
                    navController.navigate(Routes.SEARCH)
                }, onNavigateToWatchlist = {
                    navController.navigate(Routes.WATCHLIST)
                })
        }

        composable(Routes.SEARCH) {
            val vm = hiltViewModel<SearchViewModel>()
            SearchScreen(vm, onBack = { navController.popBackStack() })
        }

        composable(Routes.WATCHLIST) {
            val vm = hiltViewModel<WatchListViewModel>()
            WatchlistScreen(vm, onBack = { navController.popBackStack() })
        }

        composable("details/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
            movieId?.let {
                MovieDetailsScreen(movieId = it, navController = navController)
            }
        }

    }
}