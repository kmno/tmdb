package com.kmno.tmdb.domain.movie

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

/**
 * Created by Kamran Nourinezhad on 16 June-6 2025.
 * Copyright (c)  2025 MCI.
 */
interface MovieRepository {

    suspend fun getNowPlayingMovies(page: Int): List<Movie>

    suspend fun searchMovies(query: String): List<Movie>

    suspend fun fetchMovieDetails(movieId: Int): Flow<Movie>

    fun getWatchlist(): Flow<List<Movie>>

    suspend fun addToWatchlist(movie: Movie)
    suspend fun removeFromWatchlist(movie: Movie)

    suspend fun isInWatchlist(movieId: Int): Boolean

    fun getNowPlayingPagingFlow(): Flow<PagingData<Movie>>

}