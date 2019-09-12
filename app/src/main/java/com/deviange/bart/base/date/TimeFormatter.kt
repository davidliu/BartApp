package com.deviange.bart.base.date

import java.text.SimpleDateFormat
import java.util.*

object TimeFormatter {
    private val timeFormatter = SimpleDateFormat("h:mm", Locale.US)
    fun formatShortTime(obj: Any): String = timeFormatter.format(obj)
}