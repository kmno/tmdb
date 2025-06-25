package com.kmno.tmdb.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kmno.tmdb.presentation.auth.LoginScreen
import com.kmno.tmdb.presentation.auth.LoginViewModel
import com.kmno.tmdb.presentation.details.MovieDetailsScreen
import com.kmno.tmdb.presentation.search.SearchScreen
import com.kmno.tmdb.presentation.search.SearchViewModel
import com.kmno.tmdb.presentation.splash.SplashScreen
import com.kmno.tmdb.presentation.splash.SplashViewModel
import com.kmno.tmdb.presentation.upcoming.UpcomingScreen
import com.kmno.tmdb.presentation.upcoming.UpcomingViewModel
import com.kmno.tmdb.presentation.watchlist.WatchListViewModel
import com.kmno.tmdb.presentation.watchlist.WatchlistScreen
import kotlinx.coroutines.launch

/**
 * Created by Kamran Nourinezhad on 15 June-6 2025.
 * Copyright (c)  2025 MCI.
 */

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val UPCOMING = "upcoming"
    const val SEARCH = "search"
    const val WATCHLIST = "watchlist"
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                onDestinationClicked = { route ->
                    scope.launch { drawerState.close() }
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Routes.SPLASH
        ) {

            composable(Routes.SPLASH) {
                val userPrefs = hiltViewModel<SplashViewModel>().userPrefs
                SplashScreen(
                    userPrefs,
                    onNavigateToLogin = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onNavigateToUpcoming = {
                        navController.navigate(Routes.UPCOMING) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            composable(Routes.LOGIN) {
                val vm = hiltViewModel<LoginViewModel>()
                LoginScreen(vm, navController)
            }

            composable(Routes.UPCOMING) {
                val vm = hiltViewModel<UpcomingViewModel>()
                UpcomingScreen(
                    vm,
                    navController,
                    drawerState = drawerState,
                    onNavigateToSearch = {
                        navController.navigate(Routes.SEARCH)
                    }, onNavigateToWatchlist = {
                        navController.navigate(Routes.WATCHLIST)
                    })
            }

            composable(Routes.SEARCH) {
                val vm = hiltViewModel<SearchViewModel>()
                SearchScreen(
                    vm,
                    navController = navController,
                    onBack = { navController.popBackStack() })
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
}

//drawer
@Composable
fun DrawerContent(onDestinationClicked: (String) -> Unit) {
    ModalDrawerSheet {
        Text(
            text = "Menu",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )
        HorizontalDivider()
        DrawerItem("Upcoming", Routes.UPCOMING, onDestinationClicked)
        DrawerItem("Search", Routes.SEARCH, onDestinationClicked)
        DrawerItem("Watchlist", Routes.WATCHLIST, onDestinationClicked)
    }
}

@Composable
fun DrawerItem(label: String, route: String, onClick: (String) -> Unit) {
    ListItem(
        headlineContent = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(route) }
    )
}

