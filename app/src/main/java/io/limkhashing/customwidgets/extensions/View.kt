package io.limkhashing.customwidgets.extensions

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.view.children
import io.limkhashing.customwidgets.utils.AppConstants


fun View.avoidMultipleClicks() {
    isClickable = false
    postDelayed({ isClickable = true }, AppConstants.MULTIPLE_BUTTON_CLICK_DELAY)
}

fun View.afterLayout(what: () -> Unit) {
    if (isLaidOut) {
        what.invoke()
    } else {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                what.invoke()
            }
        })
    }
}

fun ViewGroup.disableOnTouch() {
    children.forEach {
        if (it is ViewGroup)
            it.disableOnTouch()
        else {
            it.setOnTouchListener(null)
        }
    }
    setOnTouchListener(null)
}
