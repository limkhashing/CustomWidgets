package io.limkhashing.customwidgets.extensions

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import io.limkhashing.customwidgets.R
import io.limkhashing.customwidgets.utils.DrawableUtils

fun DrawableUtils.normalButton(): Drawable {
    val enableBackground = createDrawable(
        GradientDrawable.RECTANGLE,
        R.dimen.size_3,
        R.dimen.size_1,
        R.color.transparent,
        R.color.colorAccent)
    val disableBackground = createDrawable(
        GradientDrawable.RECTANGLE,
        R.dimen.size_3,
        R.dimen.size_1,
        R.color.transparent,
        R.color.black_with_alpha_15)
    val selector = createDrawableSelector(true, enableBackground, disableBackground)
    return createRippleDrawable(selector)
}

fun DrawableUtils.transparentButton(): Drawable {
    val enableBackground = createDrawable(
        GradientDrawable.RECTANGLE,
        R.dimen.size_3,
        R.dimen.size_1,
        R.color.colorPrimary,
        R.color.white)
    val disableBackground = createDrawable(
        GradientDrawable.RECTANGLE,
        R.dimen.size_3,
        R.dimen.size_1,
        android.R.color.darker_gray,
        R.color.white)
    val selector = createDrawableSelector(true, enableBackground, disableBackground)
    return createRippleDrawable(selector)
}