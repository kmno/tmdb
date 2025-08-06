package com.kmno.tmdb

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import com.kmno.tmdb.presentation.Navigation
import com.kmno.tmdb.presentation.auth.AuthViewModel
import com.kmno.tmdb.utils.UserPreferences
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Kamran Nourinezhad on 5 August-8 2025.
 * Copyright (c)  2025 MCI.
 */
class NavigationTest {

    val testDispatcher = StandardTestDispatcher()

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    val fakePrefs = mockk<UserPreferences>(relaxed = true)

    val fakeAuthViewModel = AuthViewModel(
        userPref = fakePrefs
    )

    @Before
    fun setUp() {

    }

    @Test
    fun splashScreen_navigatesToLoginAfterDelay() {
        composeTestRule.setContent {
            Navigation(authViewModel = fakeAuthViewModel)
        }
        // Wait for the splash delay (e.g., 2000ms)
        composeTestRule.waitUntil(timeoutMillis = 2000) {
            composeTestRule.onAllNodesWithTag("sign_in_title").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("sign_in_title").assertExists()
    }
}