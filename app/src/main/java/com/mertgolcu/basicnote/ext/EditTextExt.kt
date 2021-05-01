package com.mertgolcu.basicnote.ext

import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.mertgolcu.basicnote.R

fun EditText.changeStrokeUI() {
    if (this.text.isBlank())
        this.background.overrideColor(
            ContextCompat.getColor(
                this.context,
                R.color.error_red
            )
        ) else this.background.overrideColor(
        ContextCompat.getColor(
            this.context,
            R.color.edit_text_border
        )
    )
}

fun EditText.addChangeStrokeUIListener() {
    this.addTextChangedListener {
        this.changeStrokeUI()
    }
}