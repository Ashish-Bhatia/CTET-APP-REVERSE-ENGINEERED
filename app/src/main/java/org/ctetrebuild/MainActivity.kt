package org.ctetrebuild

import android.app.Activity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.ScrollView
import android.widget.TextView

class MainActivity : Activity() {
    private lateinit var root: LinearLayout
    private var timer: CountDownTimer? = null
    private val progress by lazy { ProgressStore(this) }
    private val savedSessions by lazy { SessionPersistence(this) }
    private val history by lazy { TestHistory(this) }
    private val savedMock by lazy { MockPersistence(this) }
    private val importedBank by lazy {
        runCatching {
            val raw = assets.open("questions-demo.json").bufferedReader().use { it.readText() }
            QuestionBankJson.parse(raw)
        }.getOrNull()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showHome()
    }

    private fun screen(title: String) {
        timer?.cancel()
        timer = null
        val scroll = ScrollView(this)
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }
        scroll.addView(root)
        setContentView(scroll)
        label(title, 24f)
    }

    private fun label(text: String, size: Float = 17f): TextView {
        return TextView(this).also {
            it.text = text
            it.textSize = size
            it.setPadding(0, 12, 0, 12)
            root.addView(it)
        }
    }

    private fun action(text: String, onClick: () -> Unit) {
        root.addView(Button(this).apply {
            this.text = text
            setOnClickListener { onClick() }
        })
    }

    private fun showHome() {
        screen("CTET Practice")
        label("Choose a paper. Practice content is original demonstration material.")
        savedSessions.load()?.let { saved ->
            val track = ExamCatalog.tracks.firstOrNull { t -> t.subjects.any { it.id == saved.subjectId } }
            val subject = track?.subjects?.firstOrNull { it.id == saved.subjectId }
            if (track != null && subject != null) {
                action("Resume saved practice") { showQuiz(track, subject, saved) }
                action("Discard saved practice") { savedSessions.clear(); showHome() }
            } else savedSessions.clear()
        }
        savedMock.load()?.let { snapshot ->
            action("Resume full-paper mock") { resumeMock(snapshot) }
            action("Discard saved mock") { savedMock.clear(); showHome() }
        }
        action("View test history") { showHistory() }
        action("Full-paper mock tests") { showMockCatalog() }
        ExamCatalog.tracks.forEach { track ->
            action(track.title) { showSubjects(track) }
            progress.load(track.paper)?.let {
                label("Last result: ${it.correct}/${it.total} • ${it.attempted} attempted", 14f)
            }
        }
    }

    private fun showMockCatalog() {
        screen("Full-paper mock tests")
        label("Each mock requires 150 independently authored or properly licensed questions. Incomplete papers cannot start.")
        action("Paper I — 150 questions") { showMockReadiness(Paper.I) }
        action("Paper II — Mathematics and Science") {
            showMockReadiness(Paper.II, PaperIISpecialization.MATHEMATICS_SCIENCE)
        }
        action("Paper II — Social Studies") {
            showMockReadiness(Paper.II, PaperIISpecialization.SOCIAL_STUDIES)
        }
        action("Home") { showHome() }
    }

    private fun showMockReadiness(
        paper: Paper,
        specialization: PaperIISpecialization? = null
    ) {
        val blueprint = ExamBlueprintFactory.create(paper, specialization)
        screen("Mock test readiness")
        label("Paper ${paper.name} • ${blueprint.totalQuestions} questions • ${blueprint.durationMinutes} minutes")
        val bank = importedBank
        if (bank == null) {
            label("Bundled question bank could not be loaded. Mock test unavailable.")
        } else {
            blueprint.subjects.forEach { subject ->
                val available = bank.forSubject(subject.id).size
                label("${subject.title}: ${available}/${subject.questions} available")
            }
            val ready = runCatching { bank.forBlueprint(blueprint) }.isSuccess
            if (ready) {
                label("Question coverage complete. You can start the 150-minute mock test.")
                action("Start full-paper mock") {
                    val questions = bank.forBlueprint(blueprint)
                    savedMock.clear()
                    showFullMock(blueprint, questions)
                }
            } else {
                label("Mock test unavailable: required licensed question coverage is incomplete.")
            }
        }
        action("Back to mock tests") { showMockCatalog() }
    }

    private fun resumeMock(saved: SavedMock) {
        val blueprint = ExamBlueprintFactory.create(saved.paper, saved.specialization)
        val questions = runCatching { importedBank?.forBlueprint(blueprint) }.getOrNull()
        if (questions == null || questions.map { it.id } != saved.questionIds ||
            saved.answers.any { (id, index) ->
                questions.firstOrNull { it.id == id }?.options?.indices?.contains(index) != true
            }) {
            savedMock.clear()
            showHome()
            return
        }
        showFullMock(blueprint, questions, saved)
    }

    private fun showFullMock(blueprint: ExamBlueprint, questions: List<Question>, restored: SavedMock? = null) {
        val exam = MockExam(blueprint, questions)
        val sections = MockSections(blueprint)
        restored?.let { exam.restore(it.answers, it.markedIds, it.currentIndex) }
        val deadline = restored?.deadlineEpochMillis ?: System.currentTimeMillis() + blueprint.durationMinutes * 60_000L
        val clock = SessionClock(deadline)
        screen("Full-paper mock")
        val countdown = label("Time remaining: ${blueprint.durationMinutes}:00")
        val status = label("")
        val questionTitle = label("")
        val questionPrompt = label("")
        val choices = RadioGroup(this)
        root.addView(choices)
        var completed = false
        fun persistMock() {
            savedMock.save(SavedMock(blueprint.paper,
                if (blueprint.paper == Paper.I) null else if (blueprint.subjects.last().id == "p2-social")
                    PaperIISpecialization.SOCIAL_STUDIES else PaperIISpecialization.MATHEMATICS_SCIENCE,
                deadline, exam.quiz.questions.map { it.id }, exam.snapshotAnswers(),
                exam.snapshotMarked(), exam.navigator.index))
        }

        fun complete() {
            if (completed) return
            completed = true
            exam.submit()
            savedMock.clear()
            timer?.cancel()
            timer = null
            val result = exam.quiz.result()
            screen("Full-paper results")
            label("Paper ${blueprint.paper.name}: ${result.correct}/${result.total} correct")
            label("${result.attempted} attempted • ${result.incorrect} incorrect • ${result.unanswered} unanswered")
            label("Marked for review at submission: ${exam.markedCount}")
            label("Section-wise performance", 20f)
            MockAnalytics.sections(exam).forEach { section ->
                label("${section.subject.title}: ${section.correct}/${section.total} correct • " +
                    "${section.incorrect} incorrect • ${section.unanswered} unanswered")
            }
            action("Review answers") { showMockAnswerReview(exam) }
            action("Home") { showHome() }
        }

        fun expired(): Boolean {
            if (!clock.isExpired(System.currentTimeMillis())) return false
            complete()
            return true
        }

        fun renderQuestion() {
            if (completed) return
            val index = exam.navigator.index
            val question = exam.quiz.questions[index]
            val section = sections.sectionAt(index)
            questionTitle.text = "Question ${index + 1}/${blueprint.totalQuestions} • ${section.subject.title}"
            questionPrompt.text = question.prompt
            status.text = "Attempted: ${exam.attemptedCount} • Unanswered: ${exam.unansweredCount} • Marked: ${exam.markedCount}" +
                if (exam.isMarked(question.id)) " • Current: marked" else ""
            choices.removeAllViews()
            question.options.forEachIndexed { optionIndex, option ->
                choices.addView(RadioButton(this).apply {
                    id = View.generateViewId()
                    text = option
                    isChecked = exam.quiz.selectedIndex(question.id) == optionIndex
                    setOnClickListener {
                        if (!expired()) {
                            exam.answer(optionIndex)
                            persistMock()
                            renderQuestion()
                        }
                    }
                })
            }
        }

        sections.ranges.forEach { section ->
            action("Go to ${section.subject.title}") {
                if (!expired()) {
                    exam.navigator.goTo(section.start)
                    persistMock()
                    renderQuestion()
                }
            }
        }
        action("Previous") {
            if (!expired()) { exam.navigator.previous(); persistMock(); renderQuestion() }
        }
        action("Next") {
            if (!expired()) { exam.navigator.next(); persistMock(); renderQuestion() }
        }
        action("Clear answer") {
            if (!expired()) { exam.clearAnswer(); persistMock(); renderQuestion() }
        }
        action("Mark/unmark for review") {
            if (!expired()) { exam.toggleReviewMark(); persistMock(); renderQuestion() }
        }
        action("Submit full-paper mock") { complete() }
        action("Save and return home") {
            if (!expired()) { persistMock(); showHome() }
        }
        label("Mock progress is stored locally; the original deadline continues while away.")
        renderQuestion()
        if (expired()) return
        persistMock()
        timer = object : CountDownTimer((deadline - System.currentTimeMillis()).coerceAtLeast(1L), 1_000L) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = clock.remainingSeconds(System.currentTimeMillis())
                countdown.text = "Time remaining: ${seconds / 60}:${(seconds % 60).toString().padStart(2, '0')}"
                if (seconds == 0) complete()
            }
            override fun onFinish() { complete() }
        }.start()
    }

    private fun showMockAnswerReview(exam: MockExam) {
        screen("Full-paper answer review")
        exam.quiz.questions.forEachIndexed { index, question ->
            val selected = exam.quiz.selectedIndex(question.id)
            label("${index + 1}. ${question.prompt}")
            label("Your answer: ${selected?.let { question.options[it] } ?: "Unanswered"}")
            label("Correct answer: ${question.options[question.correctIndex]}")
        }
        action("Home") { showHome() }
    }

    private fun showSubjects(track: ExamTrack) {
        screen(track.title)
        label("Select a subject for a short practice session.")
        track.subjects.forEach { subject ->
            action("${subject.title} • ${subject.questions} exam questions") {
                savedSessions.clear()
                showQuiz(track, subject)
            }
        }
        action("Back to papers") { showHome() }
    }

    private fun showQuiz(track: ExamTrack, subject: Subject, restored: RestoredSession? = null) {
        val deadline = restored?.deadlineEpochMillis ?: (System.currentTimeMillis() + 120_000L)
        val clock = SessionClock(deadline)
        val secondsLeft = clock.remainingSeconds(System.currentTimeMillis()).coerceAtLeast(1)
        val imported = importedBank?.forSubject(subject.id).orEmpty()
        val questions = if (imported.isNotEmpty()) imported else PracticeContent.forSubject(subject.id)
        val session = PracticeSession(subject.id, questions, secondsLeft)
        val navigator = QuestionNavigator(session.quiz.questions.map { it.id })
        restored?.answers?.forEach { (id, index) ->
            val q = session.quiz.questions.firstOrNull { it.id == id }
            if (q != null && index in q.options.indices) session.answer(id, index)
        }
        fun persist() {
            val answers = session.quiz.questions.mapNotNull { q -> session.quiz.selectedIndex(q.id)?.let { q.id to it } }.toMap()
            savedSessions.save(subject.id, answers, deadline)
        }
        screen(subject.title)
        label("Demonstration practice: ${session.quiz.questions.size} questions; 2-minute original deadline.")
        label(if (imported.isNotEmpty()) "Questions loaded from bundled JSON" else "Original built-in demonstration questions", 14f)
        val countdown = label("Time remaining: ${secondsLeft / 60}:${(secondsLeft % 60).toString().padStart(2, '0')}", 16f)
        lateinit var finishSession: () -> Unit
        fun enforceDeadline(): Boolean {
            if (!clock.isExpired(System.currentTimeMillis())) return false
            finishSession()
            return true
        }
        val groups = mutableListOf<RadioGroup>()
        val prompts = mutableListOf<TextView>()
        session.quiz.questions.forEachIndexed { index, question ->
            prompts.add(label("${index + 1}. ${question.prompt}"))
            val group = RadioGroup(this)
            question.options.forEachIndexed { optionIndex, option ->
                group.addView(RadioButton(this).apply {
                    id = View.generateViewId()
                    text = option
                    setOnClickListener {
                        if (!enforceDeadline() && !session.submitted) {
                            session.answer(question.id, optionIndex)
                            persist()
                        }
                    }
                })
            }
            session.quiz.selectedIndex(question.id)?.let { selected ->
                (group.getChildAt(selected) as RadioButton).isChecked = true
            }
            root.addView(group)
            groups.add(group)
        }

        fun showCurrent() {
            groups.forEachIndexed { index, group ->
                val visibility = if (index == navigator.index) View.VISIBLE else View.GONE
                group.visibility = visibility
                prompts[index].visibility = visibility
            }
        }
        showCurrent()
        action("Previous question") {
            if (!enforceDeadline()) { navigator.previous(); showCurrent() }
        }
        action("Next question") {
            if (!enforceDeadline()) { navigator.next(); showCurrent() }
        }
        action("Clear current answer") {
            if (enforceDeadline()) return@action
            val id = navigator.currentId
            session.quiz.clear(id)
            groups[navigator.index].clearCheck()
            persist()
        }

        var finished = false
        fun finish() {
            if (finished) return
            finished = true
            if (!session.submitted) session.submit()
            timer?.cancel()
            timer = null
            savedSessions.clear()
            progress.save(track.paper, session.quiz.result())
            history.add(subject.id, session.quiz.result())
            showResult(track, subject, session)
        }

        finishSession = { finish() }
        action("Submit and review answers") { finish() }
        action("Save and return") {
            if (!enforceDeadline()) { persist(); showHome() }
        }
        action("Discard and return") { savedSessions.clear(); showHome() }
        if (clock.isExpired(System.currentTimeMillis())) { finish(); return }
        persist()
        timer = object : CountDownTimer((deadline - System.currentTimeMillis()).coerceAtLeast(1L), 1_000L) {
            override fun onTick(millisUntilFinished: Long) {
                val remaining = clock.remainingSeconds(System.currentTimeMillis())
                session.tick((session.remainingSeconds - remaining).coerceAtLeast(0))
                countdown.text = "Time remaining: ${session.remainingSeconds / 60}:${(session.remainingSeconds % 60).toString().padStart(2, '0')}"
                if (session.submitted) finish()
            }
            override fun onFinish() {
                session.tick(session.remainingSeconds)
                finish()
            }
        }.start()
    }

    private fun showResult(track: ExamTrack, subject: Subject, session: PracticeSession) {
        screen("Practice results")
        val result = session.quiz.result()
        label("Correct: ${result.correct}/${result.total}")
        label("Incorrect: ${result.incorrect} • Unanswered: ${result.unanswered}")
        session.quiz.questions.forEachIndexed { index, question ->
            val selected = session.quiz.selectedIndex(question.id)
            label("${index + 1}. ${question.prompt}")
            label("Your answer: ${selected?.let { question.options[it] } ?: "Unanswered"}")
            label("Correct answer: ${question.options[question.correctIndex]}")
        }
        action("Try again") { showQuiz(track, subject) }
        action("Subjects") { showSubjects(track) }
        action("Home") { showHome() }
    }

    private fun showHistory() {
        screen("Practice history")
        val entries = history.all()
        if (entries.isEmpty()) label("No completed practice tests yet.")
        entries.forEach { entry ->
            val subject = ExamCatalog.tracks.flatMap { it.subjects }.firstOrNull { it.id == entry.subjectId }
            label("${subject?.title ?: entry.subjectId}: ${entry.correct}/${entry.total} correct, ${entry.attempted} attempted")
        }
        action("Clear history") { history.clear(); showHistory() }
        action("Home") { showHome() }
    }

    override fun onDestroy() {
        timer?.cancel()
        super.onDestroy()
    }
}
