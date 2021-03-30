package com.mertgolcu.basicnote.extensions

import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.mertgolcu.basicnote.R

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