package org.ctetrebuild

data class SectionScore(
    val subject: Subject,
    val total: Int,
    val attempted: Int,
    val correct: Int,
    val incorrect: Int,
    val unanswered: Int
)

/** Computes section scores from the same ordered questions used by the mock. */
object MockAnalytics {
    fun sections(exam: MockExam): List<SectionScore> {
        val ranges = MockSections(exam.blueprint).ranges
        val questions = exam.quiz.questions
        return ranges.map { range ->
            val slice = questions.subList(range.start, range.endExclusive)
            val attempted = slice.count { exam.quiz.selectedIndex(it.id) != null }
            val correct = slice.count { exam.quiz.selectedIndex(it.id) == it.correctIndex }
            SectionScore(
                subject = range.subject,
                total = slice.size,
                attempted = attempted,
                correct = correct,
                incorrect = attempted - correct,
                unanswered = slice.size - attempted
            )
        }
    }
}
