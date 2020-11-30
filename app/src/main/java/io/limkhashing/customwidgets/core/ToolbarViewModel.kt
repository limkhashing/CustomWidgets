package io.limkhashing.customwidgets.core

import android.graphics.drawable.Drawable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt

interface ToolbarViewModel : BaseToolbarViewModel {

    fun getNavigationViewLayout(): Int

    fun getTitleIcon(): ObservableInt

    fun getExpandedTitle(): ObservableField<String>

    fun getExpandedSubtitle(): ObservableField<String>

    fun getToolbarBackground(): ObservableField<Drawable>
    fun getTitleColor(): ObservableInt

    fun setTitleIcon(resource: Int)
    fun setTitleColor(resource: Int)
    fun setToolbarBackground(drawable: Drawable)
    fun setExpandedTitle(s: String)
    fun setExpandedSubtitle(s: String)

    fun getExpandedTitleVisibility(): ObservableBoolean
    fun getExpandedSubtitleVisibility(): ObservableBoolean
    fun getTitleIconVisibility(): ObservableBoolean

    fun setToolbarSubtitle(s: String)
    fun getToolbarSubtitle(): ObservableField<String>
    fun getToolbarSubtitleVisibility(): ObservableBoolean
}