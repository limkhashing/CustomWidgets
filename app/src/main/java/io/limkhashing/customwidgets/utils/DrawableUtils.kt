package io.limkhashing.customwidgets.utils

import android.content.res.ColorStateList
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.drawable.*
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import io.limkhashing.customwidgets.MainApplication
import io.limkhashing.customwidgets.R

object DrawableUtils {

    fun createColorDrawable(@ColorRes colorRes : Int) : Drawable{
        val context = MainApplication.instance.applicationContext
        return ColorDrawable(context.resources.getColor(colorRes))
    }
    /**
     * @param orientation  eg: GradientDrawable.Orientation.TOP_BOTTOM
     * @param gradientType eg: GradientDrawable.LINEAR_GRADIENT
     * @param cornerRadius Do not pass value less than 1 because only accept integer
     * @param colors       Color from startFromBoost to end
     * @return Normal Drawable
     */
    fun createGradientDrawable(orientation: GradientDrawable.Orientation,
                               gradientType: Int,
                               @DimenRes cornerRadius: Int,
                               @DimenRes borderSize: Int,
                               @ColorRes borderColor: Int,
                               vararg colors: Int): GradientDrawable {
        val context = MainApplication.instance.applicationContext
        val gradientDrawable = GradientDrawable()
        gradientDrawable.orientation = orientation
        gradientDrawable.gradientType = gradientType
        if (cornerRadius != 0) {
            gradientDrawable.cornerRadius = context.resources.getDimension(cornerRadius).toInt().toFloat()
        }
        if (borderSize != 0 && borderColor != 0) {
            gradientDrawable.setStroke(context.resources.getDimension(borderSize).toInt(),
                                       ContextCompat.getColor(context, borderColor))
        }
        colors.withIndex().forEach { indexedValue: IndexedValue<Int> ->
            colors[indexedValue.index] = context.resources.getColor(indexedValue.value)
        }
        gradientDrawable.colors = colors
        return gradientDrawable
    }

    /**
     * @param shape           eg: GradientDrawable.RECTANGLE
     * @param cornerRadius    Do not pass value less than 1 because only accept integer
     * @param borderSize      Do not pass value less than 1 because only accept integer
     * @param borderColor     Border Color
     * @param backgroundColor Solid color
     * @return Normal Drawable
     */
    fun createDrawable(shape: Int,
                       @DimenRes cornerRadius: Int,
                       @DimenRes borderSize: Int,
                       @ColorRes borderColor: Int,
                       backgroundColor: Int): GradientDrawable {
        val context = MainApplication.instance.applicationContext
        val shapeDrawable = GradientDrawable()
        shapeDrawable.shape = shape
        if (cornerRadius != 0) {
            shapeDrawable.cornerRadius = context.resources.getDimension(cornerRadius)
        }
        if (borderSize != 0 && borderColor != 0) {
            shapeDrawable.setStroke(context.resources.getDimension(borderSize).toInt(),
                                    ContextCompat.getColor(context, borderColor))
        }
        if (backgroundColor > 0) {
            shapeDrawable.setColor(ContextCompat.getColor(context, backgroundColor))
        } else {
            shapeDrawable.setColor(backgroundColor)
        }
        return shapeDrawable
    }

    /**
     * @param normal Normal state
     * @param pressed Pressed state
     * @return StateListDrawable
     */
    fun createDrawableSelector(normal: Drawable,
                               pressed: Drawable): StateListDrawable {
        val stateListDrawable = StateListDrawable()
        stateListDrawable.addState(intArrayOf(android.R.attr.state_selected), pressed)
        stateListDrawable.addState(intArrayOf(android.R.attr.state_pressed), pressed)
        stateListDrawable.addState(intArrayOf(), normal)
        return stateListDrawable
    }

    /**
     * @param isForCheckedState To determine this is for enable-disable selector
     * @param normal            Either normal state or unchecked state
     * @param selected          Either selected state or checked state
     * @return StateListDrawable
     */
    fun createDrawableSelector(isForCheckedState: Boolean,
                               normal: Drawable,
                               selected: Drawable): StateListDrawable {
        val stateListDrawable = StateListDrawable()
        if (isForCheckedState) {
            stateListDrawable.addState(intArrayOf(android.R.attr.state_enabled), normal)
            stateListDrawable.addState(intArrayOf(-android.R.attr.state_enabled), selected)
        } else {
            stateListDrawable.addState(intArrayOf(android.R.attr.state_selected), selected)
            stateListDrawable.addState(intArrayOf(android.R.attr.state_checked), selected)
            stateListDrawable.addState(intArrayOf(), normal)
        }
        return stateListDrawable
    }

    /**
     * Drawable for bordered button
     * @param normalColor border color in normal state
     * @return Drawable
     */
    fun createBorderedDrawable(@ColorRes normalColor: Int,
                               @DimenRes cornerRadius: Int = R.dimen.size_3,
                               @DimenRes borderSize: Int = R.dimen.size_1): Drawable {
        val normal = createDrawable(
            GradientDrawable.RECTANGLE,
            cornerRadius,
            borderSize,
            normalColor,
            android.R.color.transparent)
        val pressed = createDrawable(
            GradientDrawable.RECTANGLE,
            cornerRadius,
            borderSize,
            R.color.transparent,
            android.R.color.transparent)
        return createDrawableSelector(normal, pressed)
    }

    /**
     * @param background background for normal view
     * @return RippleDrawable
     */
    fun createRippleDrawable(background: Drawable): Drawable {
        val context = MainApplication.instance.applicationContext
        val rippleColor = ContextCompat.getColor(context, R.color.color_control_highlight)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return RippleDrawable(
                ColorStateList.valueOf(rippleColor),
                background,
                background)
        } else {
            background.colorFilter = BlendModeColorFilter(Color.RED, BlendMode.SRC_IN)
            val states = StateListDrawable()
            states.addState(intArrayOf(android.R.attr.state_pressed), ColorDrawable(rippleColor))
            states.addState(intArrayOf(), background)
            return states
        }
    }
}