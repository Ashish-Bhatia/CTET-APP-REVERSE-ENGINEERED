package org.ctetrebuild

data class QuestionRecord(
    val id: String,
    val subjectId: String,
    val prompt: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String = "",
    val sourceLicense: String = ""
) {
    fun toQuestion() = Question(id, prompt, options, correctIndex)
}

class QuestionBank(records: List<QuestionRecord>) {
    private val items = records.toList()

    init {
        require(items.map { it.id }.distinct().size == items.size) { "Duplicate question ID" }
        items.forEach { record ->
            require(record.subjectId.isNotBlank()) { "Missing subject ID" }
            require(record.sourceLicense.isNotBlank()) { "Missing source license" }
            require(ExamCatalog.tracks.any { track -> track.subjects.any { it.id == record.subjectId } }) {
                "Unknown CTET subject: ${record.subjectId}"
            }
            record.toQuestion() // validate prompt, options and answer key
        }
    }

    fun forSubject(subjectId: String): List<Question> =
        items.filter { it.subjectId == subjectId }.map { it.toQuestion() }

    fun forBlueprint(blueprint: ExamBlueprint): List<Question> {
        val selected = blueprint.subjects.flatMap { subject ->
            val questions = forSubject(subject.id)
            require(questions.size >= subject.questions) {
                "Insufficient questions for ${subject.id}: ${questions.size}/${subject.questions}"
            }
            questions.take(subject.questions)
        }
        require(selected.size == blueprint.totalQuestions)
        return selected
    }

    val size: Int get() = items.size
}
