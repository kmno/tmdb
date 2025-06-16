package com.kmno.tmdb.data.remote

import javax.inject.Inject

/**
 * Created by Kamran Nourinezhad on 15 June-6 2025.
 * Copyright (c)  2025 MCI.
 */
class RemoteDataSource @Inject constructor(
    private val api: TmdbApi
) {
    suspend fun getNowPlayingMovies() = api.getNowPlayingMovies()
    suspend fun searchMovies(query: String) = api.searchMovies(query = query)
}