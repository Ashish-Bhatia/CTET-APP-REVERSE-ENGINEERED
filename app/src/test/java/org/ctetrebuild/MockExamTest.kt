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

    @Test fun snapshotRestoresAnswersMarksAndPosition() {
        val original = exam()
        original.answer(1)
        original.toggleReviewMark()
        original.navigator.goTo(90)
        original.answer(0)

        val recovered = exam()
        recovered.restore(original.snapshotAnswers(), original.snapshotMarked(), original.navigator.index)
        assertEquals(90, recovered.navigator.index)
        assertEquals(2, recovered.attemptedCount)
        assertEquals(1, recovered.markedCount)
        assertEquals(1, recovered.quiz.selectedIndex(original.quiz.questions[0].id))
        assertEquals(0, recovered.quiz.selectedIndex(original.quiz.questions[90].id))
    }

    @Test(expected = IllegalArgumentException::class)
    fun rejectsInvalidSavedAnswerIndex() {
        val recovered = exam()
        recovered.restore(mapOf(recovered.quiz.questions[0].id to 99), emptySet(), 0)
    }

    @Test fun invalidSnapshotDoesNotPartiallyApplyAnswers() {
        val recovered = exam()
        val first = recovered.quiz.questions[0].id
        val second = recovered.quiz.questions[1].id
        try {
            recovered.restore(linkedMapOf(first to 0, second to 99), emptySet(), 10)
            fail("Expected invalid saved option")
        } catch (_: IllegalArgumentException) {
            assertEquals(0, recovered.attemptedCount)
            assertEquals(0, recovered.navigator.index)
            assertEquals(0, recovered.markedCount)
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun rejectsUnknownSavedReviewMark() {
        exam().restore(emptyMap(), setOf("missing-question"), 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun rejectsIncompleteMock() {
        MockExam(ExamBlueprintFactory.create(Paper.I), listOf(Question("one", "Prompt", listOf("A", "B"), 0)))
    }
}
