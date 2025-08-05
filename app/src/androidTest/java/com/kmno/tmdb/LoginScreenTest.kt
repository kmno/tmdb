package com.kmno.tmdb

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import com.kmno.tmdb.domain.auth.AuthRepository
import com.kmno.tmdb.presentation.auth.LoginScreen
import com.kmno.tmdb.presentation.auth.LoginViewModel
import com.kmno.tmdb.utils.UserPreferences
import io.mockk.clearAllMocks
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Kamran Nourinezhad on 3 August-8 2025.
 * Copyright (c)  2025 MCI.
 */
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    val fakeRepo = object : AuthRepository {
        override suspend fun login(email: String, password: String): Result<Unit> {
            return Result.success(Unit)
        }
    }
    val fakePrefs = mockk<UserPreferences>(relaxed = true)
    private val navigationController = mockk<NavHostController>(relaxed = true)

    val viewModel = LoginViewModel(fakeRepo, fakePrefs)

    @Before
    fun setUp() {
        clearAllMocks()

        composeTestRule.setContent {
            LoginScreen(
                viewModel = viewModel,
                navController = navigationController
            )
        }
    }

    @Test
    fun testPageTitleIsDisplayed() {
        composeTestRule.onNodeWithTag("sign_in_title").assertIsDisplayed()
    }

    @Test
    fun testEmailPasswordLoginButtonAreDisplayed() {
        composeTestRule.onNodeWithTag("password_field").assertIsDisplayed()
        composeTestRule.onNodeWithTag("email_field").assertIsDisplayed()
        composeTestRule.onNodeWithTag("login_button").assertIsDisplayed()
    }

    @Test
    fun emailField_showsEnteredText() {
        val emailInput = "test@example.com"
        val emailField = composeTestRule.onNodeWithTag("email_field")
        emailField.performTextInput(emailInput)
        emailField.assertTextContains(emailInput)
        // Optional: If you want to assert the ViewModel's state directly
        //assertEquals(emailInput, viewModel.email.value) // Requires the setup above
    }

    @Test
    fun passwordField_showsEnteredText() {
        val passwordInput = "securePassword123"
        composeTestRule
            .onNodeWithTag("password_field")
            .performTextInput(passwordInput)
        composeTestRule
            .onNodeWithTag("password_field")
            .assertTextContains(passwordInput)
    }

    @Test
    fun errorMessage_showsOnInvalidEmail() {
        val emailInput = "email"
        val passwordInput = "securePassword123"

        val emailField = composeTestRule.onNodeWithTag("email_field")
        val passwordField = composeTestRule.onNodeWithTag("password_field")
        val loginButton = composeTestRule.onNodeWithTag("login_button")

        emailField.performTextInput(emailInput)
        passwordField.performTextInput(passwordInput)

        loginButton.performClick()

        composeTestRule.onNodeWithTag("error_message").assertIsDisplayed()
    }

    @Test
    fun errorMessage_showsOnInvalidPassword() {
        val emailInput = "test@example.com"
        val passwordInput = "pass"

        val emailField = composeTestRule.onNodeWithTag("email_field")
        val passwordField = composeTestRule.onNodeWithTag("password_field")
        val loginButton = composeTestRule.onNodeWithTag("login_button")

        emailField.performTextInput(emailInput)
        passwordField.performTextInput(passwordInput)

        loginButton.performClick()

        composeTestRule.onNodeWithTag("error_message").assertIsDisplayed()
    }

    @Test
    fun loginButton_showsLoadingAfterClick() {
        val emailInput = "test@example.com"
        val passwordInput = "securePassword123"

        val emailField = composeTestRule.onNodeWithTag("email_field")
        val passwordField = composeTestRule.onNodeWithTag("password_field")
        val loginButton = composeTestRule.onNodeWithTag("login_button")

        emailField.performTextInput(emailInput)
        passwordField.performTextInput(passwordInput)

        loginButton.performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("progress_indicator").assertIsDisplayed()
    }
}