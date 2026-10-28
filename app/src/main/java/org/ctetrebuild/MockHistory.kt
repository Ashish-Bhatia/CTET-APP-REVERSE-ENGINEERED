package org.ctetrebuild

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class MockHistoryEntry(
    val paper: Paper,
    val specialization: PaperIISpecialization?,
    val correct: Int,
    val attempted: Int,
    val completedAt: Long,
    val sections: List<SectionSummary>
)

data class SectionSummary(
    val subjectId: String,
    val correct: Int,
    val attempted: Int,
    val total: Int
)

class MockHistory(context: Context) {
    private val prefs = context.getSharedPreferences("ctet_mock_history_v1", Context.MODE_PRIVATE)
    private val key = "results"

    fun all(): List<MockHistoryEntry> {
        return try {
            val array = JSONArray(prefs.getString(key, "[]"))
            (0 until array.length()).map { i ->
                val json = array.getJSONObject(i)
                val sections = json.getJSONArray("sections")
                MockHistoryEntry(
                    paper = Paper.valueOf(json.getString("paper")),
                    specialization = json.getString("specialization").takeIf { it.isNotEmpty() }
                        ?.let(PaperIISpecialization::valueOf),
                    correct = json.getInt("correct"),
                    attempted = json.getInt("attempted"),
                    completedAt = json.getLong("completedAt"),
                    sections = (0 until sections.length()).map { j ->
                        val section = sections.getJSONObject(j)
                        SectionSummary(
                            section.getString("subjectId"),
                            section.getInt("correct"),
                            section.getInt("attempted"),
                            section.getInt("total")
                        )
                    }
                )
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun add(entry: MockHistoryEntry) {
        require(entry.correct in 0..150 && entry.attempted in entry.correct..150)
        require(entry.completedAt > 0L)
        val blueprint = ExamBlueprintFactory.create(entry.paper, entry.specialization)
        require(entry.sections.map { it.subjectId } == blueprint.subjects.map { it.id }) {
            "History sections must match the paper blueprint"
        }
        entry.sections.zip(blueprint.subjects).forEach { (section, subject) ->
            require(section.total == subject.questions)
            require(section.correct in 0..section.attempted)
            require(section.attempted in 0..section.total)
        }
        require(entry.sections.sumOf { it.total } == 150)
        require(entry.sections.sumOf { it.correct } == entry.correct)
        require(entry.sections.sumOf { it.attempted } == entry.attempted)
        val current = all().take(49)
        val array = JSONArray()
        (listOf(entry) + current).forEach { item ->
            val sections = JSONArray()
            item.sections.forEach { section ->
                sections.put(JSONObject()
                    .put("subjectId", section.subjectId)
                    .put("correct", section.correct)
                    .put("attempted", section.attempted)
                    .put("total", section.total))
            }
            array.put(JSONObject()
                .put("paper", item.paper.name)
                .put("specialization", item.specialization?.name ?: "")
                .put("correct", item.correct)
                .put("attempted", item.attempted)
                .put("completedAt", item.completedAt)
                .put("sections", sections))
        }
        check(prefs.edit().putString(key, array.toString()).commit()) {
            "Could not save mock history"
        }
    }

    fun clear() {
        check(prefs.edit().remove(key).commit()) { "Could not clear mock history" }
    }
}
