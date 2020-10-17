package io.limkhashing.customwidgets.utils

import android.view.View
import io.limkhashing.customwidgets.extensions.avoidMultipleClicks

class SingleOnClickListener(val onClickAction: (view: View) -> Unit = {}) : View.OnClickListener {

    override fun onClick(v: View?) {
        v?.let {
            it.avoidMultipleClicks()
            onClickAction.invoke(it)
        }
    }
}