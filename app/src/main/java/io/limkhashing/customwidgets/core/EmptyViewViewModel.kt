package io.limkhashing.customwidgets.core

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt

interface EmptyViewViewModel {
    fun getEmptyViewIconVisibility(): ObservableBoolean

    fun getEmptyViewIcon(): ObservableInt

    fun getEmptyViewTitle(): ObservableField<String>

    fun getEmptyViewDescription(): ObservableField<String>

    fun getEmptyViewButtonText(): ObservableField<String>

    fun onEmptyViewButtonClicked(var1: View)

    fun getEmptyViewVisibility(): ObservableBoolean

    fun setEmptyViewIconVisibility(visibility: Boolean)

    fun setEmptyViewIcon(drawableResource: Int)

    fun setEmptyViewTitle(title: String)

    fun setEmptyViewDescription(desc: String)

    fun setEmptyViewButtonText(buttonText: String)
}