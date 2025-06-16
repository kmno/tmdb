package com.kmno.tmdb.utils

import android.content.Context
import androidx.annotation.StringRes

/**
 * Created by Kamran Nourinezhad on 15 June-6 2025.
 * Copyright (c)  2025 MCI.
 */
class ResourceProvider(private val context: Context) {
    fun getString(@StringRes resId: Int): String = context.getString(resId)
}