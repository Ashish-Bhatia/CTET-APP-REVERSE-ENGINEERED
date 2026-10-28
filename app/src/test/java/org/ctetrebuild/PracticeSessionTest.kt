package org.ctetrebuild

import org.junit.Assert.*
import org.junit.Test

class PracticeSessionTest {
    private fun session() = PracticeSession("demo", listOf(
        Question("q", "Question?", listOf("A", "B"), 0)
    ), durationSeconds = 5)

    @Test fun timerExpiresAndSubmits() {
        val s = session()
        s.tick(10)
        assertEquals(0, s.remainingSeconds)
        assertTrue(s.submitted)
    }

    @Test fun answerBeforeSubmissionIsScored() {
        val s = session()
        s.answer("q", 0)
        assertEquals(1, s.submit().correct)
    }

    @Test(expected = IllegalStateException::class)
    fun answersAfterSubmissionAreRejected() {
        val s = session()
        s.submit()
        s.answer("q", 0)
    }

    @Test fun tickAfterSubmissionIsNoOp() {
        val s = session()
        s.submit()
        s.tick(3)
        assertEquals(5, s.remainingSeconds)
    }
}
