package io.limkhashing.customwidgets.widgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import io.limkhashing.customwidgets.R
import io.limkhashing.customwidgets.extensions.normalButton
import io.limkhashing.customwidgets.utils.DrawableUtils
import kotlinx.android.synthetic.main.view_action_button.view.*

class ActionButton : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var onClickedAction = {}
    var enabledButton: Boolean = true
        set(value) {
            field = value
            btn_next.isEnabled = field
        }

    var labelButton: Int = 0
        set(value) {
            field = value
//            (context?.applicationContext as FaveBizApplication).applicationDataManager.language().asObservable().subscribe {
//                btn_next.text = (context?.applicationContext as FaveBizApplication).stringProvider.getTranslatedString(value, it)
//            }
        }

    var labelTextColor: Int = 0
        set(value) {
            field = value
            btn_next.setTextColor(ContextCompat.getColor(context, value))
        }

    var backgroundButton: Drawable = DrawableUtils.normalButton()
        set(value) {
            field = value
            btn_next.background = field
        }

    init {
        View.inflate(context, R.layout.view_action_button, this)
        btn_next.background = DrawableUtils.normalButton()
        btn_next.setOnClickListener {
            onClickedAction.invoke()
        }
    }
}