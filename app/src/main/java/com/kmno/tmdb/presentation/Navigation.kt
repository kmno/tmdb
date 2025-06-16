package com.kmno.tmdb.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kmno.tmdb.presentation.upcoming.UpcomingScreen
import com.kmno.tmdb.presentation.upcoming.UpcomingViewModel

//import com.kmno.tmdb.presentation.upcoming.UpcomingViewModel

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
    // Navigation logic will go here
    // For example, you can use NavHost from androidx.navigation.compose
    // to set up your navigation graph and destinations.
    // This is a placeholder for the actual navigation implementation.

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.UPCOMING // Replace with your actual start destination
    ) {
        // Define your composable destinations here
        // composable("home") { HomeScreen(navController) }
        // composable("details/{movieId}") { backStackEntry ->
        //     DetailsScreen(movieId = backStackEntry.arguments?.getString("movieId"))
        // }

        composable(Routes.UPCOMING) {
            val vm = hiltViewModel<UpcomingViewModel>()
            UpcomingScreen(
                vm,
                onNavigateToSearch = {
                    navController.navigate(Routes.SEARCH)
                }, onNavigateToWatchlist = {
                    navController.navigate(Routes.WATCHLIST)
                })
        }

        composable(Routes.SEARCH) {
            /*val vm = hiltViewModel<SearchViewModel>()
            SearchScreen(vm, onBack = { navController.popBackStack() })*/
        }

        composable(Routes.WATCHLIST) {
            /*  val vm = hiltViewModel<WatchlistViewModel>()
              WatchlistScreen(vm, onBack = { navController.popBackStack() })*/

        }
    }
}