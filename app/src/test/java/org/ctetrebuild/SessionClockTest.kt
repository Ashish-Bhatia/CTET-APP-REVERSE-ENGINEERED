package org.ctetrebuild

import org.junit.Assert.*
import org.junit.Test

class SessionClockTest {
    @Test fun exactExpiryIsZero() {
        val clock = SessionClock(120_000L)
        assertEquals(0, clock.remainingSeconds(120_000L))
        assertTrue(clock.isExpired(120_000L))
    }

    @Test fun reopeningDoesNotResetDeadline() {
        val clock = SessionClock(120_000L)
        assertEquals(120, clock.remainingSeconds(0L))
        assertEquals(60, clock.remainingSeconds(60_000L))
        assertEquals(1, clock.remainingSeconds(119_999L))
        assertEquals(0, clock.remainingSeconds(130_000L))
    }

    @Test fun extremeDifferenceIsBounded() {
        assertEquals(Int.MAX_VALUE, SessionClock(Long.MAX_VALUE).remainingSeconds(1L))
    }

    @Test(expected = IllegalArgumentException::class)
    fun invalidDeadlineRejected() { SessionClock(0L) }
}
