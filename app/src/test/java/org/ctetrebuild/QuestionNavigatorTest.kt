package org.ctetrebuild

import org.junit.Assert.*
import org.junit.Test

class QuestionNavigatorTest {
    @Test fun navigationClampsAtEnds() {
        val nav = QuestionNavigator(listOf("a", "b"))
        assertEquals("a", nav.previous())
        assertEquals("b", nav.next())
        assertEquals("b", nav.next())
        assertTrue(nav.hasPrevious)
        assertFalse(nav.hasNext)
    }

    @Test(expected = IllegalArgumentException::class)
    fun invalidIndexIsRejected() { QuestionNavigator(listOf("a")).goTo(2) }

    @Test(expected = IllegalArgumentException::class)
    fun duplicateQuestionIdsAreRejected() { QuestionNavigator(listOf("a", "a")) }
}
