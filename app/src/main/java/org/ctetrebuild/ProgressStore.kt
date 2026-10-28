package org.ctetrebuild

import android.content.Context

data class SavedResult(
    val correct: Int,
    val attempted: Int,
    val total: Int,
    val savedAt: Long
)

class ProgressStore(context: Context) {
    private val prefs = context.getSharedPreferences("ctet_progress_v1", Context.MODE_PRIVATE)

    fun save(paper: Paper, result: QuizResult, savedAt: Long = System.currentTimeMillis()) {
        val key = paper.name
        prefs.edit()
            .putInt("${key}_correct", result.correct)
            .putInt("${key}_attempted", result.attempted)
            .putInt("${key}_total", result.total)
            .putLong("${key}_savedAt", savedAt)
            .apply()
    }

    fun load(paper: Paper): SavedResult? {
        val key = paper.name
        if (!prefs.contains("${key}_savedAt")) return null
        return SavedResult(
            correct = prefs.getInt("${key}_correct", 0),
            attempted = prefs.getInt("${key}_attempted", 0),
            total = prefs.getInt("${key}_total", 0),
            savedAt = prefs.getLong("${key}_savedAt", 0L)
        )
    }

    fun clear(paper: Paper) {
        val key = paper.name
        prefs.edit()
            .remove("${key}_correct")
            .remove("${key}_attempted")
            .remove("${key}_total")
            .remove("${key}_savedAt")
            .apply()
    }
}
