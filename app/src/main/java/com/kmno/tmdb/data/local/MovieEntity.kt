package com.kmno.tmdb.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Kamran Nourinezhad on 16 June-6 2025.
 * Copyright (c)  2025 MCI.
 */

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val releaseDate: String?
)
