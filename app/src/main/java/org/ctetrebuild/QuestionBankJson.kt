package org.ctetrebuild

import org.json.JSONArray
import org.json.JSONObject

object QuestionBankJson {
    fun parse(raw: String): QuestionBank {
        val root = JSONObject(raw)
        require(root.getInt("schemaVersion") == 1) { "Unsupported question bank schema" }
        val records = root.getJSONArray("questions")
        val questions = (0 until records.length()).map { index ->
            val item = records.getJSONObject(index)
            val options = item.getJSONArray("options").asStrings()
            QuestionRecord(
                id = item.getString("id"),
                subjectId = item.getString("subjectId"),
                prompt = item.getString("prompt"),
                options = options,
                correctIndex = item.getInt("correctIndex"),
                explanation = item.optString("explanation", ""),
                sourceLicense = item.getString("sourceLicense")
            )
        }
        return QuestionBank(questions)
    }

    private fun JSONArray.asStrings(): List<String> =
        (0 until length()).map { index -> getString(index) }
}
