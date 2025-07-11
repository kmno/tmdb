package com.kmno.tmdb.presentation.auth

import app.cash.turbine.test
import com.kmno.tmdb.utils.UserPreferences
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Kamran Nourinezhad on 8 July-7 2025.
 * Copyright (c)  2025 MCI.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val userPref = mockk<UserPreferences>(relaxed = true)
    private lateinit var authTokenFlow: MutableStateFlow<String?>
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setup() {
        clearAllMocks()

        authTokenFlow = MutableStateFlow(null)
        every { userPref.authToken } returns authTokenFlow

        viewModel = AuthViewModel(userPref)
    }

    /**
     * Tests the behavior of the `isLoggedIn` flow when the authToken changes.
     * Verifies that the flow emits the correct values based on the authToken state.
     */
    @Test
    fun `is loggedInFlow acts correctly when authToken is set`() = runTest {

        // The viewModel.isLoggedIn is already set up correctly thanks to the @Before method

        // Tests the `isLoggedIn` flow using Turbine.
        viewModel.isLoggedIn.test {

            // Verifies that the initial value emitted is false.
            assertEquals(false, awaitItem())

            // Updates the authToken to a non-null value and verifies the emitted value is true.
            authTokenFlow.value = "new_token"
            assertEquals(true, awaitItem())

            // Updates the authToken to "null" and verifies the emitted value is false.
            authTokenFlow.value = null
            assertEquals(false, awaitItem())

            // Cancels and ignores any remaining events in the flow.
            cancelAndIgnoreRemainingEvents()
        }
    }
}