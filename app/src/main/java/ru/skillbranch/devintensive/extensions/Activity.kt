package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText

fun Activity.hideKeyboard() {
    val inputManager: InputMethodManager =
        this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (this.currentFocus != null) {
        inputManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
        inputManager.hideSoftInputFromInputMethod(
            this.currentFocus?.windowToken,
            0
        )
    }
}

fun Activity.isKeyboardOpen(): Boolean {
    val rootView = findViewById<View>(android.R.id.content)
    val rect = Rect()
    rootView.getWindowVisibleDisplayFrame(rect)
    val heightDiff: Int = rootView.rootView.height - (rect.bottom - rect.top)
    return heightDiff > 100
}

fun Activity.isKeyboardClosed(): Boolean {
    return this.isKeyboardOpen().not()
}

fun AppCompatEditText.onClickKeyboardDoneButton(execute: () -> Unit) {
    this.setOnEditorActionListener { _, actionId, _ ->
        when (actionId) {
            EditorInfo.IME_ACTION_DONE -> {
                execute.invoke()
                true
            }
            else -> false
        }
    }
}