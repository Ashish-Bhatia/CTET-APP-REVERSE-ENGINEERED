package org.ctetrebuild

data class Question(
    val id: String,
    val prompt: String,
    val options: List<String>,
    val correctIndex: Int
) {
    init {
        require(id.isNotBlank())
        require(prompt.isNotBlank())
        require(options.size >= 2)
        require(options.all { it.isNotBlank() })
        require(correctIndex in options.indices)
    }
}

data class QuizResult(
    val total: Int,
    val attempted: Int,
    val correct: Int,
    val incorrect: Int,
    val unanswered: Int
) {
    val score: Int get() = correct
}

class QuizEngine(questions: List<Question>) {
    val questions: List<Question> = questions.toList()
    private val answers = mutableMapOf<String, Int>()

    init {
        require(this.questions.isNotEmpty())
        require(this.questions.map { it.id }.distinct().size == this.questions.size)
    }

    fun answer(questionId: String, optionIndex: Int) {
        val question = questions.firstOrNull { it.id == questionId }
            ?: throw IllegalArgumentException("Unknown question")
        require(optionIndex in question.options.indices)
        answers[questionId] = optionIndex
    }

    fun clear(questionId: String) {
        require(questions.any { it.id == questionId })
        answers.remove(questionId)
    }

    fun selectedIndex(questionId: String): Int? = answers[questionId]

    fun result(): QuizResult {
        val correct = questions.count { answers[it.id] == it.correctIndex }
        val attempted = answers.size
        return QuizResult(
            total = questions.size,
            attempted = attempted,
            correct = correct,
            incorrect = attempted - correct,
            unanswered = questions.size - attempted
        )
    }
}
