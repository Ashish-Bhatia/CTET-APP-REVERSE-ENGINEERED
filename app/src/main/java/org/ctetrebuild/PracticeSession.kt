package org.ctetrebuild

data class SessionSnapshot(
    val subjectId: String,
    val remainingSeconds: Int,
    val result: QuizResult,
    val submitted: Boolean
)

class PracticeSession(
    val subjectId: String,
    questions: List<Question>,
    durationSeconds: Int = 600
) {
    val quiz = QuizEngine(questions)
    var remainingSeconds: Int = durationSeconds
        private set
    var submitted: Boolean = false
        private set

    init {
        require(subjectId.isNotBlank())
        require(durationSeconds > 0)
    }

    fun answer(questionId: String, index: Int) {
        check(!submitted) { "Session already submitted" }
        quiz.answer(questionId, index)
    }

    fun tick(seconds: Int = 1) {
        require(seconds >= 0)
        if (submitted) return
        remainingSeconds = (remainingSeconds.toLong() - seconds).coerceAtLeast(0).toInt()
        if (remainingSeconds == 0) submit()
    }

    fun submit(): QuizResult {
        submitted = true
        return quiz.result()
    }

    fun snapshot() = SessionSnapshot(subjectId, remainingSeconds, quiz.result(), submitted)
}
