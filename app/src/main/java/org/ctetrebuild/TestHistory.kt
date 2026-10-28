package org.ctetrebuild

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class HistoryEntry(
    val subjectId: String,
    val correct: Int,
    val total: Int,
    val attempted: Int,
    val completedAt: Long
)

class TestHistory(context: Context) {
    private val prefs = context.getSharedPreferences("ctet_test_history_v1", Context.MODE_PRIVATE)
    private val key = "results"

    fun all(): List<HistoryEntry> {
        return try {
            val data = JSONArray(prefs.getString(key, "[]"))
            (0 until data.length()).map { i ->
                val item = data.getJSONObject(i)
                HistoryEntry(
                    item.getString("subjectId"),
                    item.getInt("correct"),
                    item.getInt("total"),
                    item.getInt("attempted"),
                    item.getLong("completedAt")
                )
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun add(subjectId: String, result: QuizResult, completedAt: Long = System.currentTimeMillis()) {
        require(subjectId.isNotBlank())
        val existing = all().take(49)
        val array = JSONArray()
        val entry = JSONObject()
        entry.put("subjectId", subjectId)
        entry.put("correct", result.correct)
        entry.put("total", result.total)
        entry.put("attempted", result.attempted)
        entry.put("completedAt", completedAt)
        array.put(entry)
        existing.forEach { old ->
            array.put(JSONObject().apply {
                put("subjectId", old.subjectId)
                put("correct", old.correct)
                put("total", old.total)
                put("attempted", old.attempted)
                put("completedAt", old.completedAt)
            })
        }
        prefs.edit().putString(key, array.toString()).apply()
    }

    fun clear() { prefs.edit().remove(key).apply() }
}
