package org.ctetrebuild

import org.junit.Assert.*
import org.junit.Test

class MockExamTest {
    private fun exam(): MockExam {
        val blueprint = ExamBlueprintFactory.create(Paper.I)
        val questions = blueprint.subjects.flatMap { subject ->
            (1..subject.questions).map { index ->
                Question("${subject.id}-${index}", "Original fixture?", listOf("Yes", "No"), 0)
            }
        }
        return MockExam(blueprint, questions)
    }

    @Test fun startsWith150Unanswered() {
        val exam = exam()
        assertEquals(150, exam.quiz.questions.size)
        assertEquals(150, exam.unansweredCount)
        assertEquals(0, exam.attemptedCount)
    }

    @Test fun answerAndReviewMarkAreIndependent() {
        val exam = exam()
        exam.answer(0)
        assertEquals(1, exam.attemptedCount)
        assertTrue(exam.toggleReviewMark())
        assertEquals(1, exam.markedCount)
        assertFalse(exam.toggleReviewMark())
        assertEquals(0, exam.markedCount)
        exam.clearAnswer()
        assertEquals(0, exam.attemptedCount)
    }

    @Test fun canNavigateToLastQuestion() {
        val exam = exam()
        exam.navigator.goTo(149)
        exam.answer(1)
        assertEquals(150, exam.navigator.index + 1)
        assertEquals(1, exam.attemptedCount)
    }

    @Test(expected = IllegalStateException::class)
    fun cannotAnswerAfterSubmission() {
        val exam = exam()
        exam.submit()
        exam.answer(0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun rejectsIncompleteMock() {
        MockExam(ExamBlueprintFactory.create(Paper.I), listOf(Question("one", "Prompt", listOf("A", "B"), 0)))
    }
}
