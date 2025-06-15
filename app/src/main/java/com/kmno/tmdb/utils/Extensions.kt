/*
 * Last Modified: 15/6/2025 3:7:7,
 * Copyright (c) 2025 . Kamran N. Farvin
 */

import java.text.SimpleDateFormat
import java.util.*

fun String.toReadableDate(): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        formatter.format(parser.parse(this)!!)
    } catch (e: Exception) {
        this
    }
}
