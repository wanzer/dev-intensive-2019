package ru.skillbranch.devintensive.ui.profile

import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_profile.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel

class ProfileActivity : AppCompatActivity() {

    private var editEnable = false
    private lateinit var viewFields: Map<String, TextView>
    private lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initViews(savedInstanceState)
        initViewModel()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_EDIT_MODE, editEnable)
    }

    private fun initViews(savedInstanceState: Bundle?){
        viewFields = mapOf(
            "nickName" to tv_nick_name,
            "rank" to tv_rank,
            "rating" to tv_rating,
            "respect" to tv_respect,
            "firstName" to et_first_name,
            "lastName" to et_last_name,
            "about" to et_about,
            "repository" to et_repository
        )

        editEnable = savedInstanceState?.getBoolean(IS_EDIT_MODE, false)?: false
        showCurrentMode(editEnable)

        btn_edit.setOnClickListener {
            if (editEnable) saveProfileInfo()
            editEnable = editEnable.not()
            showCurrentMode(editEnable)
        }

        btn_switch_theme.setOnClickListener {
            viewModel.switchTheme()
        }
    }

    private fun initViewModel(){
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.getProfileData().observe(this, Observer {
            updateUI(profile = it)
        })
        viewModel.getTheme().observe(this, Observer {
            updateTheme(it)
        })
    }

    private fun updateTheme(themeMode: Int){
        delegate.localNightMode = themeMode
    }

    private fun updateUI(profile: Profile){
        profile.toMap().also {
            for((k, v) in viewFields){
                v.text = it[k].toString()
            }
        }

        Log.d("ProfileActivity", "nick_name: ${profile.toMap().get("nickName")}")
    }

    private fun showCurrentMode(isEdit: Boolean) {
        val info = viewFields.filter { setOf("firstName", "lastName", "about", "repository").contains(it.key)}
        for((_, v) in info){
            v as EditText
            v.isEnabled = isEdit
            v.isFocusable = isEdit
            v.isFocusableInTouchMode = isEdit
            v.background.alpha = if (isEdit) 255 else 0

            visible_iv.visibility = if(isEdit) View.GONE else View.VISIBLE
            wr_about.isCounterEnabled = isEdit

            with(btn_edit){
                val colorFilter: ColorFilter? = if (isEdit)
                    PorterDuffColorFilter(resources.getColor(R.color.color_accent, theme),
                    PorterDuff.Mode.SRC_IN)
                else null

                val icon = if (isEdit) resources.getDrawable(R.drawable.ic_save_black_24dp, theme)
                else resources.getDrawable(R.drawable.ic_edit_black_24dp, theme)

                background.colorFilter = colorFilter
                setImageDrawable(icon)
            }
        }
    }

    private fun saveProfileInfo(){
        val profile = Profile(
            firstName = et_first_name.text.toString(),
            lastName = et_last_name.text.toString(),
            about = et_about.text.toString(),
            repository = et_repository.text.toString()
        ).apply {
            viewModel.saveProfileData(this)
        }
    }

    companion object {
        const val IS_EDIT_MODE = "IS_EDIT_MODE"
    }
}
