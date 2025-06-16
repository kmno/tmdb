package com.kmno.tmdb.domain

/**
 * Created by Kamran Nourinezhad on 16 June-6 2025.
 * Copyright (c)  2025 MCI.
 */
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val releaseDate: String?
)
