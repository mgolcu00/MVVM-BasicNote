package com.mertgolcu.basicnote.extensions

import android.content.res.Resources
import android.graphics.drawable.*
import android.widget.EditText
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.mertgolcu.basicnote.R

fun Drawable.overrideColor(@ColorInt colorInt: Int) {
    when (this) {
        is GradientDrawable -> setStroke(1, colorInt)
        is ShapeDrawable -> paint.color = colorInt
        is ColorDrawable -> color = colorInt
        is StateListDrawable -> {
            val dcs = this.constantState as DrawableContainer.DrawableContainerState
            val drawableItems = dcs.children
//            val gradientDrawableFocused = drawableItems[0] as GradientDrawable
            val gradientDrawableDefault = drawableItems[1] as GradientDrawable

            gradientDrawableDefault.setStroke(1.dp, colorInt)
        }
    }
}

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

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
