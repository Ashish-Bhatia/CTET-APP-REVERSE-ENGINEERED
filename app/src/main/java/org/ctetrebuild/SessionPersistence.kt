package org.ctetrebuild

import android.content.Context
import org.json.JSONObject

data class RestoredSession(
    val subjectId: String,
    val answers: Map<String, Int>,
    val deadlineEpochMillis: Long
)

class SessionPersistence(context: Context) {
    private val prefs = context.getSharedPreferences("ctet_active_session_v1", Context.MODE_PRIVATE)

    fun save(subjectId: String, answers: Map<String, Int>, deadlineEpochMillis: Long) {
        require(subjectId.isNotBlank())
        require(deadlineEpochMillis > 0)
        val json = JSONObject()
        json.put("version", 1)
        json.put("subjectId", subjectId)
        json.put("deadlineEpochMillis", deadlineEpochMillis)
        val encoded = JSONObject()
        answers.forEach { (id, option) -> encoded.put(id, option) }
        json.put("answers", encoded)
        prefs.edit().putString("active", json.toString()).apply()
    }

    fun load(): RestoredSession? {
        val raw = prefs.getString("active", null) ?: return null
        return try {
            val json = JSONObject(raw)
            if (json.getInt("version") != 1) return null
            val subject = json.getString("subjectId")
            val deadline = json.getLong("deadlineEpochMillis")
            if (subject.isBlank() || deadline <= 0) return null
            val encoded = json.getJSONObject("answers")
            val answers = mutableMapOf<String, Int>()
            val keys = encoded.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                answers[key] = encoded.getInt(key)
            }
            RestoredSession(subject, answers, deadline)
        } catch (_: Exception) {
            clear()
            null
        }
    }

    fun clear() { prefs.edit().remove("active").apply() }
}
