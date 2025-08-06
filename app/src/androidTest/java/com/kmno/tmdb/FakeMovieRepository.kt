package com.kmno.tmdb

import androidx.paging.PagingData
import com.kmno.tmdb.domain.movie.Movie
import com.kmno.tmdb.domain.movie.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

/**
 * Created by Kamran Nourinezhad on 5 August-8 2025.
 * Copyright (c)  2025 MCI.
 */
class FakeMovieRepository : MovieRepository {

    private val watchlist = MutableStateFlow(
        listOf(
            Movie(
                id = 1, title = "Fake Movie",
                overview = "Fake Overview",
                posterPath = "Fake Path",
                releaseDate = "Fake Date"
            ),
            Movie(
                id = 2, title = "Fake Movie",
                overview = "Fake Overview",
                posterPath = "Fake Path",
                releaseDate = "Fake Date"
            ),
            Movie(
                id = 3, title = "Fake Movie",
                overview = "Fake Overview",
                posterPath = "Fake Path",
                releaseDate = "Fake Date"
            )
        )
    )
    private val movieDetails = MutableStateFlow<Movie?>(null)

    override suspend fun getNowPlayingMovies(page: Int): List<Movie> {
        return listOf(
            Movie(
                id = 1, title = "Fake Movie",
                overview = "Fake Overview",
                posterPath = "Fake Path",
                releaseDate = "Fake Date"
            ),
            Movie(
                id = 2, title = "Fake Movie",
                overview = "Fake Overview",
                posterPath = "Fake Path",
                releaseDate = "Fake Date"
            ),
            Movie(
                id = 3, title = "Fake Movie",
                overview = "Fake Overview",
                posterPath = "Fake Path",
                releaseDate = "Fake Date"
            )
        ) // Use test data
    }

    override suspend fun searchMovies(query: String): List<Movie> {
        return listOf(
            Movie(
                id = 1, title = "Fake Movie",
                overview = "Fake Overview",
                posterPath = "Fake Path",
                releaseDate = "Fake Date"
            )
        )
    }

    override suspend fun fetchMovieDetails(movieId: Int): Flow<Movie> {
        return flow {
            emit(
                Movie(
                    id = 1, title = "Fake Movie",
                    overview = "Fake Overview",
                    posterPath = "Fake Path",
                    releaseDate = "Fake Date"
                )
            )
        }
    }

    override fun getWatchlist(): Flow<List<Movie>> = watchlist

    override suspend fun addToWatchlist(movie: Movie) {
        watchlist.value = watchlist.value + movie
    }

    override suspend fun removeFromWatchlist(movie: Movie) {
        watchlist.value = watchlist.value - movie
    }

    override suspend fun isInWatchlist(movieId: Int): Boolean {
        return watchlist.value.any { it.id == movieId }
    }

    override fun getNowPlayingPagingFlow(): Flow<PagingData<Movie>> {
        return flowOf(
            PagingData.from(
                listOf(
                    Movie(
                        id = 1, title = "Fake Movie 1",
                        overview = "Fake Overview",
                        posterPath = "Fake Path",
                        releaseDate = "Fake Date"
                    ),
                    Movie(
                        id = 2, title = "Fake Movie 2",
                        overview = "Fake Overview",
                        posterPath = "Fake Path",
                        releaseDate = "Fake Date"
                    ),
                    Movie(
                        id = 3, title = "Fake Movie 3",
                        overview = "Fake Overview",
                        posterPath = "Fake Path",
                        releaseDate = "Fake Date"
                    )
                )
            )
        )
    }
}
