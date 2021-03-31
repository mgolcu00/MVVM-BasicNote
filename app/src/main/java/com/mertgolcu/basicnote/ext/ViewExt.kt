package com.mertgolcu.basicnote.ext

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.controlAllEditTextStrokeUI() {
    if (this is ViewGroup) {
        val size = this.childCount
        for (i in 0 until size) {
            when (val child = this.getChildAt(i)) {
                is EditText -> {
                    child.changeStrokeUI()
                }
            }
        }
    }
}

fun View.addAllEditTextStrokeUIListener() {
    if (this is ViewGroup) {
        val size = this.childCount
        for (i in 0 until size) {
            when (val child = this.getChildAt(i)) {
                is EditText -> {
                    child.addChangeStrokeUIListener()
                }
            }
        }
    }
}