package com.kmno.tmdb.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Kamran Nourinezhad on 15 June-6 2025.
 * Copyright (c)  2025 MCI.
 */
interface TmdbApi {
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("region") region: String = "CA",  // Canada
        @Query("page") page: Int = 1
    ): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
    ): MovieDto

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): MovieResponse
}

data class MovieResponse(
    val page: Int,
    val results: List<MovieDto>,
    val total_pages: Int,
    val total_results: Int
)