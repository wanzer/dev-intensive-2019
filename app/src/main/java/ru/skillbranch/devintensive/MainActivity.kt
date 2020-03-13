package ru.skillbranch.devintensive

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.model.Bender
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var bender: Bender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val status = savedInstanceState?.getString("STATUS")?: Bender.Status.NORMAL.name
        val question = savedInstanceState?.getString("QUESTION")?: Bender.Question.NAME.name
        bender = Bender(status = Bender.Status.valueOf(status), question = Bender.Question.valueOf(question))
        val (r,g,b) = bender.status.color
        iv_bender.setColorFilter(Color.rgb(r,g,b), PorterDuff.Mode.MULTIPLY)
        iv_send_question.setOnClickListener(this)
        tv_bender_question.text = bender.askQuestion()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.iv_send_question -> {
                val (phrase, color) = bender.listenAnswer(et_message.text.toString().toLowerCase(Locale.getDefault()))
                et_message.setText("")
                val (r,g,b) = color
                iv_bender.setColorFilter(Color.rgb(r,g,b), PorterDuff.Mode.MULTIPLY)
                tv_bender_question.text = phrase
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("STATUS", bender.status.name)
        outState.putString("QUESTION", bender.question.name)
    }
}
