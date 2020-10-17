package io.limkhashing.customwidgets.utils

import android.graphics.drawable.GradientDrawable
import io.limkhashing.customwidgets.R

class LayoutUtils {

    companion object {
        @JvmStatic
        fun goldGradientButton() = DrawableUtils.createGradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                GradientDrawable.LINEAR_GRADIENT,
                R.dimen.size_30,
                R.dimen.size_2,
                R.color.gold,
                R.color.gold_gradient_0,
                R.color.gold_gradient_1
        )

        @JvmStatic
        fun whiteButton() = DrawableUtils.createDrawable(
                GradientDrawable.RECTANGLE,
                R.dimen.size_30,
                0, 0,
                R.color.white
        )

        @JvmStatic
        fun blueButton() = DrawableUtils.createDrawable(
                GradientDrawable.RECTANGLE,
                R.dimen.size_30,
                0,
                0,
                R.color.deep_blue
        )

    }
}