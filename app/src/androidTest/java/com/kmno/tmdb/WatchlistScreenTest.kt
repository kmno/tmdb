package com.kmno.tmdb

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.kmno.tmdb.presentation.watchlist.WatchListViewModel
import com.kmno.tmdb.presentation.watchlist.WatchlistScreen
import io.mockk.clearAllMocks
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Kamran Nourinezhad on 5 August-8 2025.
 * Copyright (c)  2025 MCI.
 */
class WatchlistScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    val fakeRepo = FakeMovieRepository()
    val viewModel = WatchListViewModel(fakeRepo)

    @Before
    fun setup() {
        clearAllMocks()

        composeTestRule.setContent {
            WatchlistScreen(
                viewModel = viewModel,
                onBack = {}
            )
        }
    }

    @Test
    fun movieList_isShown() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("movie_lazy_list").assertIsDisplayed()
    }
}