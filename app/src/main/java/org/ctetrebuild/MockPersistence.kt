package org.ctetrebuild

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class SavedMock(
    val paper: Paper,
    val specialization: PaperIISpecialization?,
    val deadlineEpochMillis: Long,
    val questionIds: List<String>,
    val answers: Map<String, Int>,
    val markedIds: Set<String>,
    val currentIndex: Int
)

class MockPersistence(context: Context) {
    private val prefs = context.getSharedPreferences("ctet_mock_v1", Context.MODE_PRIVATE)

    fun save(saved: SavedMock) {
        require(saved.deadlineEpochMillis > 0L)
        require(saved.questionIds.size == 150 && saved.questionIds.distinct().size == 150)
        require(saved.currentIndex in saved.questionIds.indices)
        require(saved.answers.keys.all { it in saved.questionIds })
        require(saved.markedIds.all { it in saved.questionIds })
        val json = JSONObject()
            .put("version", 1)
            .put("paper", saved.paper.name)
            .put("specialization", saved.specialization?.name ?: "")
            .put("deadline", saved.deadlineEpochMillis)
            .put("index", saved.currentIndex)
            .put("ids", JSONArray(saved.questionIds))
        val answers = JSONObject()
        saved.answers.forEach { (id, option) -> answers.put(id, option) }
        json.put("answers", answers)
        json.put("marked", JSONArray(saved.markedIds.toList()))
        check(prefs.edit().putString("active", json.toString()).commit()) { "Could not save mock progress" }
    }

    fun load(): SavedMock? {
        val raw = prefs.getString("active", null) ?: return null
        return try {
            val json = JSONObject(raw)
            require(json.getInt("version") == 1)
            val paper = Paper.valueOf(json.getString("paper"))
            val specialization = json.getString("specialization").takeIf { it.isNotEmpty() }
                ?.let(PaperIISpecialization::valueOf)
            val blueprint = ExamBlueprintFactory.create(paper, specialization)
            val idsArray = json.getJSONArray("ids")
            val ids = (0 until idsArray.length()).map { idsArray.getString(it) }
            require(ids.size == blueprint.totalQuestions && ids.distinct().size == ids.size)
            val answersJson = json.getJSONObject("answers")
            val answers = mutableMapOf<String, Int>()
            val keys = answersJson.keys()
            while (keys.hasNext()) {
                val id = keys.next()
                answers[id] = answersJson.getInt(id)
            }
            val marks = json.getJSONArray("marked")
            val marked = (0 until marks.length()).map { marks.getString(it) }.toSet()
            val index = json.getInt("index")
            val deadline = json.getLong("deadline")
            require(deadline > 0 && index in ids.indices)
            require(answers.keys.all { it in ids } && marked.all { it in ids })
            SavedMock(paper, specialization, deadline, ids, answers, marked, index)
        } catch (_: Exception) {
            clear()
            null
        }
    }

    fun clear() { prefs.edit().remove("active").commit() }
}
