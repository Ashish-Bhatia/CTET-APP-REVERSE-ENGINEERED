package org.ctetrebuild

import org.junit.Assert.*
import org.junit.Test

class QuestionBankTest {
    private fun record(id: String, subject: String = "p1-cdp") =
        QuestionRecord(id, subject, "Sample prompt", listOf("Yes", "No"), 0, sourceLicense = "Original")

    @Test fun subjectFilteringWorks() {
        val bank = QuestionBank(listOf(record("one"), record("two", "p1-maths")))
        assertEquals(1, bank.forSubject("p1-cdp").size)
        assertEquals(0, bank.forSubject("missing").size)
    }

    @Test(expected = IllegalArgumentException::class)
    fun duplicateIdsRejected() {
        QuestionBank(listOf(record("same"), record("same")))
    }

    @Test(expected = IllegalArgumentException::class)
    fun missingLicenseRejected() {
        QuestionBank(listOf(record("x").copy(sourceLicense = "")))
    }

    @Test(expected = IllegalArgumentException::class)
    fun unknownSubjectRejected() {
        QuestionBank(listOf(record("unknown", "not-a-ctet-subject")))
    }

    @Test(expected = IllegalArgumentException::class)
    fun incompleteFullPaperRejected() {
        QuestionBank(listOf(record("one"))).forBlueprint(ExamBlueprintFactory.create(Paper.I))
    }

    @Test fun completePaperIsExactly150Questions() {
        val records = ExamCatalog.paperI.subjects.flatMap { subject ->
            (1..subject.questions).map { index -> record("${subject.id}-${index}", subject.id) }
        }
        val questions = QuestionBank(records).forBlueprint(ExamBlueprintFactory.create(Paper.I))
        assertEquals(150, questions.size)
        assertEquals(150, questions.map { it.id }.distinct().size)
    }
}
