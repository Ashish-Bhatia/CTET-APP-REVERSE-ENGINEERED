package org.ctetrebuild

import org.junit.Assert.*
import org.junit.Test

class QuestionBankJsonTest {
    private fun document(question: String) =
        """{"schemaVersion":1,"questions":[$question]}"""

    private val valid =
        """{"id":"demo-1","subjectId":"p1-cdp","prompt":"Example?","options":["A","B"],"correctIndex":0,"sourceLicense":"Original"}"""

    @Test fun parsesValidQuestion() {
        val bank = QuestionBankJson.parse(document(valid))
        assertEquals(1, bank.size)
        assertEquals("demo-1", bank.forSubject("p1-cdp").single().id)
    }

    @Test(expected = IllegalArgumentException::class)
    fun rejectsUnsupportedSchema() {
        QuestionBankJson.parse("""{"schemaVersion":2,"questions":[]}""")
    }

    @Test(expected = IllegalArgumentException::class)
    fun rejectsDuplicateIds() {
        QuestionBankJson.parse(document("$valid,$valid"))
    }

    @Test(expected = IllegalArgumentException::class)
    fun rejectsMissingLicense() {
        QuestionBankJson.parse(document(valid.replace("Original", "")))
    }

    @Test(expected = IllegalArgumentException::class)
    fun rejectsInvalidAnswerIndex() {
        QuestionBankJson.parse(document(valid.replace("\"correctIndex\":0", "\"correctIndex\":9")))
    }
}
