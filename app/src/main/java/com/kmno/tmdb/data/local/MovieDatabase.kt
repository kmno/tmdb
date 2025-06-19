package com.kmno.tmdb.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Created by Kamran Nourinezhad on 16 June-6 2025.
 * Copyright (c)  2025 MCI.
 */

@Database(entities = [MovieEntity::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}