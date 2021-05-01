package com.mertgolcu.basicnote.ext

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.mertgolcu.basicnote.R
import com.mertgolcu.basicnote.api.ErrorResponse

fun String.handleErrorJson(): ErrorResponse? {
    return Gson().fromJson(
        this,
        ErrorResponse::class.java
    )
}

fun String.showSnackBar(view: View, colorId: Int = R.color.error_red) {
    Snackbar.make(
        view,
        this,
        Snackbar.LENGTH_LONG
    )
        .setBackgroundTint(
            ContextCompat.getColor(
                view.context,
                colorId
            )
        )
        .show()
}

inline fun String.textSpanColor(crossinline listener: () -> Unit): SpannableString {
    val content = SpannableString(this)
    content.setSpan(
        object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = Color.parseColor("#8B8CFF")
            }

            override fun onClick(p0: View) {
                listener()
            }
        },
        this.indexOf('?') + 1,
        this.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return content
}
