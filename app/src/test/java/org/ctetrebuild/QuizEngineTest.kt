package org.ctetrebuild

import org.junit.Assert.*
import org.junit.Test

class QuizEngineTest {
    private fun engine() = QuizEngine(listOf(
        Question("q1", "First?", listOf("A", "B"), 0),
        Question("q2", "Second?", listOf("A", "B"), 1)
    ))

    @Test fun tracksCorrectIncorrectAndUnanswered() {
        val quiz = engine()
        quiz.answer("q1", 0)
        quiz.answer("q2", 0)
        assertEquals(1, quiz.result().correct)
        assertEquals(1, quiz.result().incorrect)
        assertEquals(0, quiz.result().unanswered)
    }

    @Test fun replacingAnswerDoesNotDoubleCount() {
        val quiz = engine()
        quiz.answer("q1", 1)
        quiz.answer("q1", 0)
        assertEquals(1, quiz.result().attempted)
        assertEquals(1, quiz.result().correct)
    }

    @Test fun clearingAnswerRestoresUnanswered() {
        val quiz = engine()
        quiz.answer("q1", 0)
        quiz.clear("q1")
        assertEquals(2, quiz.result().unanswered)
    }

    @Test(expected = IllegalArgumentException::class)
    fun rejectsInvalidOption() { engine().answer("q1", 3) }

    @Test(expected = IllegalArgumentException::class)
    fun rejectsDuplicateQuestionIds() {
        QuizEngine(listOf(
            Question("x", "One", listOf("A", "B"), 0),
            Question("x", "Two", listOf("A", "B"), 1)
        ))
    }
}
