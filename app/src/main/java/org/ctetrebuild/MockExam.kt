package org.ctetrebuild

/**
 * Android-independent 150-question mock test state.
 * Questions are supplied only after QuestionBank.forBlueprint validates coverage.
 */
class MockExam(
    val blueprint: ExamBlueprint,
    questions: List<Question>
) {
    val quiz = QuizEngine(questions)
    val navigator = QuestionNavigator(questions.map { it.id })
    private val marked = mutableSetOf<String>()
    var submitted = false
        private set

    init {
        require(questions.size == blueprint.totalQuestions) { "Incomplete mock exam" }
    }

    fun answer(optionIndex: Int) {
        check(!submitted) { "Mock already submitted" }
        quiz.answer(navigator.currentId, optionIndex)
    }

    fun clearAnswer() {
        check(!submitted) { "Mock already submitted" }
        quiz.clear(navigator.currentId)
    }

    fun toggleReviewMark(): Boolean {
        check(!submitted) { "Mock already submitted" }
        val id = navigator.currentId
        if (!marked.add(id)) marked.remove(id)
        return id in marked
    }

    fun isMarked(questionId: String): Boolean = questionId in marked

    val markedCount: Int get() = marked.size
    val attemptedCount: Int get() = quiz.result().attempted
    val unansweredCount: Int get() = quiz.result().unanswered

    fun submit(): QuizResult {
        submitted = true
        return quiz.result()
    }
}
