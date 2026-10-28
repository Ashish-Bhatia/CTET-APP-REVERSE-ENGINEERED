package org.ctetrebuild

object PracticeContent {
    // Independently authored demonstration items, not official CTET questions.
    fun forSubject(subjectId: String): List<Question> = when {
        subjectId.endsWith("cdp") -> listOf(
            Question("cdp-1", "Which method encourages active construction of knowledge?",
                listOf("Inquiry-based learning", "Copying answers", "Avoiding questions", "Only memorising definitions"), 0),
            Question("cdp-2", "Formative assessment primarily helps teachers to:",
                listOf("Rank learners permanently", "Adjust teaching using feedback", "Replace instruction", "Avoid observation"), 1)
        )
        subjectId.endsWith("maths") || subjectId.endsWith("maths-science") -> listOf(
            Question("math-1", "What is 3/4 of 20?",
                listOf("10", "12", "15", "18"), 2),
            Question("math-2", "Which number is prime?",
                listOf("21", "27", "29", "33"), 2)
        )
        else -> listOf(
            Question("general-1", "Which practice supports inclusive classroom participation?",
                listOf("One teaching approach for all", "Flexible learning activities", "Excluding struggling learners", "No learner feedback"), 1),
            Question("general-2", "A useful learning objective should be:",
                listOf("Unobservable", "Ambiguous", "Measurable", "Unrelated to lessons"), 2)
        )
    }
}
