package ru.skillbranch.devintensive

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.extensions.hideKeyboard
import ru.skillbranch.devintensive.extensions.onClickKeyboardDoneButton
import ru.skillbranch.devintensive.models.Bender
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var bender: Bender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val status = savedInstanceState?.getString("STATUS")?: Bender.Status.NORMAL.name
        val question = savedInstanceState?.getString("QUESTION")?: Bender.Question.NAME.name
        val wrongQuestionCounter = savedInstanceState?.getInt("WRONG_QUESTION_COUNTER")?: 0

        bender = Bender(status = Bender.Status.valueOf(status), question = Bender.Question.valueOf(question))

        bender.wrongAnswerCounter = wrongQuestionCounter
        val (r,g,b) = bender.status.color
        iv_bender.setColorFilter(Color.rgb(r,g,b), PorterDuff.Mode.MULTIPLY)

        iv_send_question.setOnClickListener(this)
        tv_bender_question.text = bender.askQuestion()

        et_message.onClickKeyboardDoneButton(execute = {sendMessage()})
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.iv_send_question -> sendMessage()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("STATUS", bender.status.name)
        outState.putString("QUESTION", bender.question.name)
        outState.putInt("WRONG_QUESTION_COUNTER", bender.wrongAnswerCounter)
    }

    private fun sendMessage() {
        hideKeyboard()
        if (!et_message?.text.isNullOrEmpty() && et_message.text?.isNotBlank()!!){
            val (phrase, color) = bender.listenAnswer(et_message.text?.trim().toString())
            et_message.setText("")
            val (r,g,b) = color
            iv_bender.setColorFilter(Color.rgb(r,g,b), PorterDuff.Mode.MULTIPLY)
            tv_bender_question.text = phrase
        }
    }
}
