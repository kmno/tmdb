package com.kmno.tmdb

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToKey
import androidx.navigation.NavHostController
import com.kmno.tmdb.presentation.upcoming.UpcomingScreen
import com.kmno.tmdb.presentation.upcoming.UpcomingViewModel
import com.kmno.tmdb.utils.ConnectivityObserver
import io.mockk.clearAllMocks
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Kamran Nourinezhad on 5 August-8 2025.
 * Copyright (c) 2025 MCI.
 */
class UpcomingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    val connectivityObserver = mockk<ConnectivityObserver>(relaxed = true)
    val fakeRepo = FakeMovieRepository()
    val navigationController = mockk<NavHostController>(relaxed = true)

    val viewModel = UpcomingViewModel(fakeRepo, connectivityObserver)

    @Before
    fun setup() {
        clearAllMocks()

        composeTestRule.setContent {
            UpcomingScreen(
                viewModel = viewModel,
                navigationController,
                drawerState = null
            )
        }
    }

    @Test
    fun initialLoadingIndicator_isShown() {
        composeTestRule.onNodeWithTag("initial_loading_indicator").assertIsDisplayed()
    }

    @Test
    fun upcomingMovieList_isShown() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("upcoming_movies_list").assertIsDisplayed()
    }

    @Test
    fun upcomingMovieList_isScrolled() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("upcoming_movies_list").performScrollToKey(3)
        composeTestRule.onNodeWithText("Fake Movie 3").assertIsDisplayed()
    }
}