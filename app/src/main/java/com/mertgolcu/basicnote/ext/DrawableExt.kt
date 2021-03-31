package com.mertgolcu.basicnote.ext

import android.content.res.Resources
import android.graphics.drawable.*
import androidx.annotation.ColorInt

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



val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
