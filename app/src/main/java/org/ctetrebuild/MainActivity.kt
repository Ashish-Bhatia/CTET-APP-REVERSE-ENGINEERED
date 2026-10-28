package org.ctetrebuild

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.ScrollView
import android.widget.TextView

class MainActivity : Activity() {
    private val quiz = QuizEngine(
        listOf(
            Question("demo1", "Which approach supports learning through active participation?",
                listOf("Rote repetition only", "Learner-centred activities", "No feedback", "Passive listening only"), 1),
            Question("demo2", "Which assessment is used to guide ongoing teaching?",
                listOf("Formative assessment", "Only final examination", "Entrance screening only", "None"), 0)
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scroll = ScrollView(this)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(28, 32, 28, 32)
        }
        scroll.addView(layout)
        val heading = TextView(this).apply {
            text = "CTET Practice • Demo"
            textSize = 24f
        }
        layout.addView(heading)
        val result = TextView(this).apply { textSize = 18f }
        quiz.questions.forEachIndexed { index, question ->
            layout.addView(TextView(this).apply {
                text = "${index + 1}. ${question.prompt}"
                textSize = 18f
                setPadding(0, 28, 0, 8)
            })
            val group = RadioGroup(this)
            question.options.forEachIndexed { optionIndex, option ->
                group.addView(RadioButton(this).apply {
                    id = View.generateViewId()
                    text = option
                    setOnClickListener { quiz.answer(question.id, optionIndex) }
                })
            }
            layout.addView(group)
        }
        layout.addView(Button(this).apply {
            text = "Submit practice test"
            setOnClickListener {
                val score = quiz.result()
                result.text = "Score: ${score.correct}/${score.total} | Attempted: ${score.attempted} | Unanswered: ${score.unanswered}"
            }
        })
        layout.addView(result)
        setContentView(scroll)
    }
}
