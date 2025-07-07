package com.kmno.tmdb.presentation.auth

/**
 * Created by Kamran Nourinezhad on 6 July-7 2025.
 * Copyright (c)  2025 MCI.
 */

import com.kmno.tmdb.domain.auth.AuthRepository
import com.kmno.tmdb.utils.UserPreferences
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val repository = mockk<AuthRepository>()
    private val userPrefs = mockk<UserPreferences>(relaxed = true)
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        clearAllMocks()
        viewModel = LoginViewModel(repository, userPrefs)
    }

    @Test
    fun `test login with invalid email credentials`() = runTest {
        // Arrange
        viewModel.email = "invalid-email"
        viewModel.password = "123456"

        //action
        viewModel.login {}

        //verify
        assertEquals("Invalid email or password", viewModel.errorMessage)
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `test login with invalid password credentials`() = runTest {
        // Arrange
        viewModel.email = "test@example.com"
        viewModel.password = "123"

        //action
        viewModel.login {}

        //verify
        assertEquals("Invalid email or password", viewModel.errorMessage)
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `test login with valid credentials`() = runTest {
        // Arrange
        coEvery { repository.login("test@example.com", "123456") } returns Result.success(Unit)
        coEvery { userPrefs.saveToken(any()) } returns Unit

        var onSuccessCalled = false
        viewModel.email = "test@example.com"
        viewModel.password = "123456"

        //action
        viewModel.login { onSuccessCalled = true }

        // Wait for all coroutines launched in viewModelScope to finish
        advanceUntilIdle()

        //verify
        assertFalse(viewModel.isLoading)
        assertTrue(onSuccessCalled)
        assertNull(viewModel.errorMessage)
        coVerify { userPrefs.saveToken(any()) }
    }

}