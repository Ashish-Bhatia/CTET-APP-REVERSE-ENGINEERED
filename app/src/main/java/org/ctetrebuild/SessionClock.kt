package org.ctetrebuild

/** Absolute wall-clock deadline: reopening a session cannot reset its duration. */
class SessionClock(val deadlineEpochMillis: Long) {
    init { require(deadlineEpochMillis > 0L) }

    fun remainingSeconds(nowEpochMillis: Long): Int {
        if (nowEpochMillis >= deadlineEpochMillis) return 0
        // Saturate on subtraction overflow (for example Long.MAX_VALUE minus a negative time).
        val rawDifference = deadlineEpochMillis - nowEpochMillis
        val millis = if (nowEpochMillis < 0L && rawDifference < 0L) Long.MAX_VALUE else rawDifference
        val seconds = millis / 1000L + if (millis % 1000L != 0L) 1L else 0L
        return seconds.coerceAtMost(Int.MAX_VALUE.toLong()).toInt()
    }

    fun isExpired(nowEpochMillis: Long): Boolean = nowEpochMillis >= deadlineEpochMillis
}
