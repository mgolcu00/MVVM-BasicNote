package com.mertgolcu.basicnote.extensions

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

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
