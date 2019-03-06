package com.dotpad2.utils

import java.util.*
import java.util.concurrent.TimeUnit

object TimeUtils {

    fun calculateTimeAgo(createdTime: Long): String {
        val currentTime = Calendar.getInstance().timeInMillis
        val diff = (currentTime - createdTime)
        return when {
            diff < TimeUnit.MINUTES.toMillis(1) -> "${TimeUnit.MILLISECONDS.toSeconds(diff)}s"
            diff < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(diff)}m"
            diff < TimeUnit.DAYS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toHours(diff)}h"
            else -> "${TimeUnit.MILLISECONDS.toDays(diff)}d"
        }
    }
}