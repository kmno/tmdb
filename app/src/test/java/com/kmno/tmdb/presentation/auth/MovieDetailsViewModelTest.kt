package com.kmno.tmdb.presentation.auth

import app.cash.turbine.test
import com.kmno.tmdb.domain.movie.Movie
import com.kmno.tmdb.domain.movie.MovieRepository
import com.kmno.tmdb.presentation.details.MovieDetailsViewModel
import com.kmno.tmdb.utils.ConnectivityObserver
import com.kmno.tmdb.utils.UiState
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

/**
 * Created by Kamran Nourinezhad on 9 July-7 2025.
 * Copyright (c)  2025 MCI.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailsViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: MovieDetailsViewModel
    private val repository: MovieRepository = mockk()
    private val connectivityObserver: ConnectivityObserver = mockk(relaxed = true)

    private val _isWatchlistProcessing = MutableStateFlow(false)

    @Before
    fun setup() {
        clearAllMocks()
        coEvery { repository.getWatchlist() } returns flowOf(emptyList())
        viewModel = MovieDetailsViewModel(repository, connectivityObserver)
    }

    @Test
    fun `load Movie Details Emits Success When Movie Is Fetched`() = runTest {
        val movie = Movie(
            1,
            "Test Movie",
            "Description",
            "PosterPath",
            "2025-07-09"
        )

        // Mock the repository's fetchMovieDetails method to return the mock movie.
        coEvery { repository.fetchMovieDetails(1) } returns flowOf(movie)

        // Trigger the ViewModel's loadMovieDetails method.
        viewModel.loadMovieDetails(1)

        // Test the movieDetails flow using Turbine.
        viewModel.movieDetails.test {
            // Assert that the first emitted state is Loading.
            assertTrue(awaitItem() is UiState.Loading)

            // Assert that the second emitted state is Success and contains the correct movie data.
            val successState = awaitItem()
            assertTrue(successState is UiState.Success)
            assertEquals(movie, (successState as UiState.Success).data)

            // Cancel and ignore any remaining events in the flow.
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `load Movie Details Emits Error When IO Exception Occurs`() = runTest {
        coEvery { repository.fetchMovieDetails(1) } throws IOException("No internet connection")

        viewModel.loadMovieDetails(1)

        viewModel.movieDetails.test {
            assertTrue(awaitItem() is UiState.Loading)

            val errorState = awaitItem()
            assertTrue(errorState is UiState.Error)
            assertEquals("No internet connection", (errorState as UiState.Error).message)

            cancelAndIgnoreRemainingEvents()
        }

    }

}