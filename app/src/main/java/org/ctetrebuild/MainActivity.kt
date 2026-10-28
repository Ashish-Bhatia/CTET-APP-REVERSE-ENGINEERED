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
        ExamCatalog.tracks.forEach { track ->
            action(track.title) { showSubjects(track) }
            progress.load(track.paper)?.let {
                label("Last result: ${it.correct}/${it.total} • ${it.attempted} attempted", 14f)
            }
        }
    }

    private fun showSubjects(track: ExamTrack) {
        screen(track.title)
        label("Select a subject for a short practice session.")
        track.subjects.forEach { subject ->
            action("${subject.title} • ${subject.questions} exam questions") {
                showQuiz(track, subject)
            }
        }
        action("Back to papers") { showHome() }
    }

    private fun showQuiz(track: ExamTrack, subject: Subject) {
        val session = PracticeSession(subject.id, PracticeContent.forSubject(subject.id), 120)
        screen(subject.title)
        label("Demonstration practice: ${session.quiz.questions.size} questions, 2 minutes.")
        val countdown = label("Time remaining: 02:00", 16f)
        val groups = mutableListOf<RadioGroup>()
        session.quiz.questions.forEachIndexed { index, question ->
            label("${index + 1}. ${question.prompt}")
            val group = RadioGroup(this)
            question.options.forEachIndexed { optionIndex, option ->
                group.addView(RadioButton(this).apply {
                    id = View.generateViewId()
                    text = option
                    setOnClickListener {
                        if (!session.submitted) session.answer(question.id, optionIndex)
                    }
                })
            }
            root.addView(group)
            groups.add(group)
        }

        fun finish() {
            if (!session.submitted) session.submit()
            timer?.cancel()
            timer = null
            progress.save(track.paper, session.quiz.result())
            showResult(track, subject, session)
        }

        action("Submit and review answers") { finish() }
        action("Cancel practice") { showSubjects(track) }

        timer = object : CountDownTimer(120_000L, 1_000L) {
            override fun onTick(millisUntilFinished: Long) {
                val elapsed = ((120_000L - millisUntilFinished) / 1000L).toInt()
                session.tick(elapsed - (120 - session.remainingSeconds))
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

    override fun onDestroy() {
        timer?.cancel()
        super.onDestroy()
    }
}
