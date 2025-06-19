package com.kmno.tmdb.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Created by Kamran Nourinezhad on 16 June-6 2025.
 * Copyright (c)  2025 MCI.
 */
@Dao
interface MovieDao {

    @Query("SELECT * FROM movies")
    fun getWatchlist(): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWatchlist(movie: MovieEntity)

    @Delete
    suspend fun removeFromWatchlist(movie: MovieEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM movies WHERE id = :movieId)")
    suspend fun isInWatchlist(movieId: Int): Boolean

}